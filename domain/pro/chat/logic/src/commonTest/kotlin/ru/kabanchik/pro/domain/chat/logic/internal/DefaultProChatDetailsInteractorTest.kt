package ru.kabanchik.pro.domain.chat.logic.internal

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDateTime
import ru.kabanchik.common.chat.model.CommonAttachment
import ru.kabanchik.common.chat.model.CommonChatError
import ru.kabanchik.common.chat.model.CommonChatErrorCode
import ru.kabanchik.common.chat.model.CommonChatMessage
import ru.kabanchik.common.chat.model.CommonMessage
import ru.kabanchik.common.chat.model.CommonMessageType
import ru.kabanchik.common.chat.model.CommonSessionMessage
import ru.kabanchik.common.chat.model.CommonSessionStatus
import ru.kabanchik.common.domain.chat.logic.api.CommonChatDetailsInteractor
import ru.kabanchik.common.files.api.ReadableFile
import ru.kabanchik.pro.domain.chat.logic.api.ExecutorOffShiftException
import ru.kabanchik.pro.domain.chat.logic.api.ProEndChatException
import ru.kabanchik.pro.domain.chat.logic.api.repository.ProChatDetailsRepository
import ru.kabanchik.pro.domain.shift.logic.api.ProShiftInteractor
import ru.kabanchik.pro.domain.shift.model.ProShiftStatus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertTrue

class DefaultProChatDetailsInteractorTest {
    @Test
    fun endsChatWhenServerSendsClosedSession() = runTest {
        val repository = EndChatProChatDetailsRepository(
            onEndChat = { sessionId -> emitSession(sessionId, CommonSessionStatus.Closed) }
        )

        createInteractor(repository).endChat(SessionId)

        assertEquals(listOf("endChat:$SessionId"), repository.calls)
    }

    @Test
    fun endsChatWhenServerSendsSessionEndMessage() = runTest {
        val repository = EndChatProChatDetailsRepository(
            onEndChat = { sessionId -> emitSessionEnd(sessionId) }
        )

        createInteractor(repository).endChat(SessionId)

        assertEquals(listOf("endChat:$SessionId"), repository.calls)
    }

    @Test
    fun ignoresEventsOfOtherSessions() = runTest {
        val repository = EndChatProChatDetailsRepository(
            serverStatus = CommonSessionStatus.Open,
            onEndChat = {
                emitSession(OtherSessionId, CommonSessionStatus.Closed)
                emitSessionEnd(OtherSessionId)
                emitError(OtherSessionId, CommonChatErrorCode.NotSessionExecutor)
            }
        )

        val error = assertFailsWith<ProEndChatException> {
            createInteractor(repository).endChat(SessionId)
        }

        // Ответа по своей заявке нет: по таймауту проверили состояние через REST
        assertNull(error.code)
        assertEquals(listOf("endChat:$SessionId", "getChatStatus:$SessionId"), repository.calls)
    }

    @Test
    fun treatsChatAsEndedWhenServerFailedButClosedIt() = runTest {
        val repository = EndChatProChatDetailsRepository(
            serverStatus = CommonSessionStatus.Closed,
            onEndChat = { sessionId -> emitError(sessionId, CommonChatErrorCode.SessionEndFailed) }
        )

        createInteractor(repository).endChat(SessionId)

        assertEquals(listOf("endChat:$SessionId", "getChatStatus:$SessionId"), repository.calls)
    }

    @Test
    fun failsWhenServerFailedAndChatIsStillOpen() = runTest {
        val repository = EndChatProChatDetailsRepository(
            serverStatus = CommonSessionStatus.Open,
            onEndChat = { sessionId -> emitError(sessionId, CommonChatErrorCode.SessionEndFailed) }
        )

        val error = assertFailsWith<ProEndChatException> {
            createInteractor(repository).endChat(SessionId)
        }

        assertEquals(CommonChatErrorCode.SessionEndFailed, error.code)
    }

    @Test
    fun checksStatusWhenServerDoesNotRespond() = runTest {
        val repository = EndChatProChatDetailsRepository(serverStatus = CommonSessionStatus.Closed)

        createInteractor(repository).endChat(SessionId)

        assertEquals(listOf("endChat:$SessionId", "getChatStatus:$SessionId"), repository.calls)
    }

    @Test
    fun marksOffShiftWhenServerRejectsEnding() = runTest {
        val repository = EndChatProChatDetailsRepository(
            onEndChat = { sessionId -> emitError(sessionId, CommonChatErrorCode.ExecutorOffShift) }
        )
        val shiftInteractor = OnShiftInteractor()

        assertFailsWith<ExecutorOffShiftException> {
            createInteractor(repository, shiftInteractor).endChat(SessionId)
        }

        assertEquals(ProShiftStatus.OffShift, shiftInteractor.shiftStatus.value)
    }

    @Test
    fun failsWithoutStatusCheckWhenExecutorIsNotAssigned() = runTest {
        val repository = EndChatProChatDetailsRepository(
            onEndChat = { sessionId -> emitError(sessionId, CommonChatErrorCode.NotSessionExecutor) }
        )

        val error = assertFailsWith<ProEndChatException> {
            createInteractor(repository).endChat(SessionId)
        }

        assertEquals(CommonChatErrorCode.NotSessionExecutor, error.code)
        assertTrue("getChatStatus:$SessionId" !in repository.calls)
    }

    private fun createInteractor(
        repository: EndChatProChatDetailsRepository,
        shiftInteractor: ProShiftInteractor = OnShiftInteractor(),
    ): DefaultProChatDetailsInteractor {
        return DefaultProChatDetailsInteractor(
            commonInteractor = UnusedCommonChatDetailsInteractor(),
            detailsRepository = repository,
            shiftInteractor = shiftInteractor
        )
    }

    private companion object {
        const val SessionId = "session-1"
        const val OtherSessionId = "session-2"
    }
}

/**
 * Имитирует сервер: на endChat присылает события, заданные в [onEndChat].
 */
private class EndChatProChatDetailsRepository(
    private val serverStatus: CommonSessionStatus = CommonSessionStatus.Open,
    private val onEndChat: suspend EndChatProChatDetailsRepository.(String) -> Unit = {},
) : ProChatDetailsRepository {
    val calls = mutableListOf<String>()
    private val sessions = MutableSharedFlow<CommonSessionMessage>(extraBufferCapacity = 64)
    private val sessionEnds = MutableSharedFlow<CommonMessage>(extraBufferCapacity = 64)
    private val errors = MutableSharedFlow<CommonChatError>(extraBufferCapacity = 64)

    suspend fun emitSession(sessionId: String, status: CommonSessionStatus) {
        sessions.emit(CommonSessionMessage(sessionId = sessionId, status = status))
    }

    suspend fun emitSessionEnd(sessionId: String) {
        sessionEnds.emit(
            CommonMessage(
                id = "system-$sessionId",
                sessionId = sessionId,
                authorLogin = "SYSTEM",
                text = "Сессия завершена",
                type = CommonMessageType.System,
                time = LocalDateTime(2026, 11, 5, 10, 0),
            )
        )
    }

    suspend fun emitError(sessionId: String, code: CommonChatErrorCode) {
        errors.emit(CommonChatError(code = code, content = "", sessionId = sessionId))
    }

    override suspend fun endChat(sessionId: String) {
        calls += "endChat:$sessionId"
        onEndChat(sessionId)
    }

    override suspend fun getChatStatus(sessionId: String): CommonSessionStatus {
        calls += "getChatStatus:$sessionId"
        return serverStatus
    }

    override suspend fun listenSession(): Flow<CommonSessionMessage> = sessions

    override suspend fun listenSessionEnd(): Flow<CommonMessage> = sessionEnds

    override suspend fun listenErrors(): Flow<CommonChatError> = errors

    override suspend fun reconnect(sessionId: String) = error("Not used")

    override suspend fun getMessages(sessionId: String): List<CommonMessage> = error("Not used")

    override suspend fun uploadFile(
        sessionId: String,
        fileName: String,
        contentType: String,
        file: ReadableFile,
    ): CommonAttachment = error("Not used")

    override suspend fun downloadFile(sessionId: String, fileId: String): ByteArray = error("Not used")

    override suspend fun sendMessage(
        sessionId: String,
        clientMessageId: String,
        content: String?,
        attachmentIds: List<String>,
    ) = error("Not used")

    override suspend fun listenMessages(): Flow<CommonMessage> = error("Not used")
}

private class OnShiftInteractor : ProShiftInteractor {
    private val _shiftStatus = MutableStateFlow<ProShiftStatus?>(ProShiftStatus.OnShift)
    override val shiftStatus: StateFlow<ProShiftStatus?> = _shiftStatus

    override suspend fun refresh(): ProShiftStatus = ProShiftStatus.OnShift

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

private class UnusedCommonChatDetailsInteractor : CommonChatDetailsInteractor {
    override suspend fun reconnect(sessionId: String) = error("Not used")

    override suspend fun listenReconnections(): Flow<Unit> = error("Not used")

    override suspend fun getMessages(sessionId: String): List<CommonChatMessage> = error("Not used")

    override suspend fun uploadFile(
        sessionId: String,
        fileName: String,
        contentType: String,
        file: ReadableFile,
    ): CommonAttachment = error("Not used")

    override suspend fun downloadFile(sessionId: String, fileId: String): ByteArray = error("Not used")

    override suspend fun sendMessage(
        sessionId: String,
        content: String?,
        attachmentIds: List<String>,
    ) = error("Not used")

    override suspend fun listenMessages(sessionId: String): Flow<CommonChatMessage> = error("Not used")

    override suspend fun getStatus(sessionId: String): CommonSessionStatus = error("Not used")

    override suspend fun listenClosed(sessionId: String): Flow<Unit> = error("Not used")
}
