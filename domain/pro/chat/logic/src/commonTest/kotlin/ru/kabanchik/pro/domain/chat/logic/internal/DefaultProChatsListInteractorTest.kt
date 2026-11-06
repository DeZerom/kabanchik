package ru.kabanchik.pro.domain.chat.logic.internal

import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import ru.kabanchik.common.chat.model.CommonChatError
import ru.kabanchik.common.chat.model.CommonChatErrorCode
import ru.kabanchik.common.chat.model.CommonChatSummary
import ru.kabanchik.common.chat.model.CommonMessage
import ru.kabanchik.common.chat.model.CommonSessionMessage
import ru.kabanchik.common.chat.model.CommonSessionStatus
import ru.kabanchik.common.domain.chat.logic.api.CommonChatsListInteractor
import ru.kabanchik.pro.domain.chat.logic.api.ExecutorOffShiftException
import ru.kabanchik.pro.domain.chat.logic.api.repository.ProChatsListRepository
import ru.kabanchik.pro.domain.chatDetails.model.ProIncoming
import ru.kabanchik.pro.domain.shift.logic.api.ProShiftInteractor
import ru.kabanchik.pro.domain.shift.model.ProShiftStatus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class DefaultProChatsListInteractorTest {
    @Test
    fun acceptsFirstIncomingChat() {
        runTimeoutTest {
            val repository = FakeProChatsListRepository(queue = listOf("session-1"))
            val interactor = createInteractor(repository)

            val session = interactor.requestChat()

            assertEquals("session-1", session.sessionId)
            assertEquals(listOf("register", "acceptChat:session-1"), repository.calls)
        }
    }

    @Test
    fun skipsChatAcceptedByAnotherOperator() {
        runTimeoutTest {
            val repository = FakeProChatsListRepository(
                queue = listOf("session-1", "session-2"),
                unavailableSessionIds = setOf("session-1")
            )
            val interactor = createInteractor(repository)

            val session = interactor.requestChat()

            assertEquals("session-2", session.sessionId)
            assertEquals(
                listOf("register", "acceptChat:session-1", "acceptChat:session-2"),
                repository.calls
            )
        }
    }

    @Test
    fun failsAndMarksOffShiftWhenServerRejectsRegistration() {
        runTimeoutTest {
            val repository = FakeProChatsListRepository(queue = emptyList(), isOffShift = true)
            val shiftInteractor = FakeShiftInteractor(serverStatus = ProShiftStatus.OffShift)
            val interactor = createInteractor(repository, shiftInteractor)

            assertFailsWith<ExecutorOffShiftException> { interactor.requestChat() }
            assertEquals(ProShiftStatus.OffShift, shiftInteractor.shiftStatus.value)
        }
    }

    @Test
    fun registersAgainAfterReconnectionOnShift() {
        runTimeoutTest {
            val repository = FakeProChatsListRepository(queue = emptyList())
            val interactor = createInteractor(repository)

            val request = async(start = CoroutineStart.UNDISPATCHED) { interactor.requestChat() }
            repository.reconnections.subscriptionCount.first { it > 0 }
            repository.queue = listOf("session-1")
            repository.reconnections.emit(Unit)

            assertEquals("session-1", request.await().sessionId)
            assertEquals(listOf("register", "register", "acceptChat:session-1"), repository.calls)
        }
    }

    @Test
    fun failsWithoutRegistrationAfterReconnectionOffShift() {
        runTimeoutTest {
            val repository = FakeProChatsListRepository(queue = emptyList())
            val shiftInteractor = FakeShiftInteractor(serverStatus = ProShiftStatus.OnShift)
            val interactor = createInteractor(repository, shiftInteractor)

            val request = async(start = CoroutineStart.UNDISPATCHED) {
                runCatching { interactor.requestChat() }
            }
            repository.reconnections.subscriptionCount.first { it > 0 }
            shiftInteractor.serverStatus = ProShiftStatus.OffShift
            repository.reconnections.emit(Unit)

            assertFailsWith<ExecutorOffShiftException> { request.await().getOrThrow() }
            assertEquals(listOf("register"), repository.calls)
        }
    }

    private fun runTimeoutTest(block: suspend kotlinx.coroutines.CoroutineScope.() -> Unit) {
        runBlocking {
            withTimeout(5_000) { block() }
        }
    }

    private fun createInteractor(
        repository: FakeProChatsListRepository,
        shiftInteractor: FakeShiftInteractor = FakeShiftInteractor(serverStatus = ProShiftStatus.OnShift)
    ): DefaultProChatsListInteractor {
        return DefaultProChatsListInteractor(
            commonInteractor = StubCommonChatsListInteractor(),
            listRepository = repository,
            shiftInteractor = shiftInteractor
        )
    }
}

/**
 * Имитирует сервер: на register присылает очередь заявок, на acceptChat - принятый чат или ошибку.
 */
private class FakeProChatsListRepository(
    var queue: List<String>,
    private val unavailableSessionIds: Set<String> = emptySet(),
    private val isOffShift: Boolean = false
) : ProChatsListRepository {
    val calls = mutableListOf<String>()
    val reconnections = MutableSharedFlow<Unit>()
    private val incoming = MutableSharedFlow<ProIncoming>(extraBufferCapacity = 64)
    private val sessions = MutableSharedFlow<CommonSessionMessage>(extraBufferCapacity = 64)
    private val errors = MutableSharedFlow<CommonChatError>(extraBufferCapacity = 64)

    override suspend fun register() {
        calls += "register"
        if (isOffShift) {
            errors.emit(CommonChatError(code = CommonChatErrorCode.ExecutorOffShift, content = ""))
            return
        }
        queue.forEach { incoming.emit(ProIncoming(sessionId = it, clientLogin = "user_77")) }
    }

    override suspend fun listenReconnections(): Flow<Unit> = reconnections

    override suspend fun listenIncoming(): Flow<ProIncoming> = incoming

    override suspend fun acceptChat(sessionId: String) {
        calls += "acceptChat:$sessionId"
        if (sessionId in unavailableSessionIds) {
            errors.emit(
                CommonChatError(
                    code = CommonChatErrorCode.SessionNotAvailable,
                    content = "",
                    sessionId = sessionId
                )
            )
        } else {
            sessions.emit(
                CommonSessionMessage(
                    sessionId = sessionId,
                    status = CommonSessionStatus.Open,
                    participantLogin = "user_77",
                    participantName = "user_77",
                    participantRole = "USER"
                )
            )
        }
    }

    override suspend fun listenSession(): Flow<CommonSessionMessage> = sessions

    override suspend fun listenErrors(): Flow<CommonChatError> = errors
}

private class FakeShiftInteractor(
    var serverStatus: ProShiftStatus
) : ProShiftInteractor {
    private val _shiftStatus = MutableStateFlow<ProShiftStatus?>(null)
    override val shiftStatus: StateFlow<ProShiftStatus?> = _shiftStatus

    override suspend fun refresh(): ProShiftStatus {
        _shiftStatus.value = serverStatus
        return serverStatus
    }

    override suspend fun startShift(): ProShiftStatus = ProShiftStatus.OnShift

    override suspend fun endShift(): ProShiftStatus = ProShiftStatus.OffShift

    override suspend fun logout() = Unit

    override fun markOffShift() {
        _shiftStatus.value = ProShiftStatus.OffShift
    }

    override fun clear() {
        _shiftStatus.value = null
    }
}

private class StubCommonChatsListInteractor : CommonChatsListInteractor {
    override suspend fun connect() = Unit

    override suspend fun listenReconnections(): Flow<Unit> = emptyFlow()

    override suspend fun getChats(): List<CommonChatSummary> = emptyList()

    override suspend fun listenMessages(): Flow<CommonMessage> = emptyFlow()
}
