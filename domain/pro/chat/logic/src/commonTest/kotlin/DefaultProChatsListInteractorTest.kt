package ru.kabanchik.pro.domain.chat.logic.internal

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import ru.kabanchik.common.chat.model.CommonChatSummary
import ru.kabanchik.common.chat.model.CommonMessage
import ru.kabanchik.common.chat.model.CommonSessionMessage
import ru.kabanchik.common.chat.model.CommonSessionStatus
import ru.kabanchik.common.domain.chat.logic.api.CommonChatsListInteractor
import ru.kabanchik.pro.domain.chat.logic.api.repository.ProChatsListRepository
import ru.kabanchik.pro.domain.chatDetails.model.ProIncoming
import kotlin.test.Test
import kotlin.test.assertEquals

class DefaultProChatsListInteractorTest {
    @Test
    fun subscribesBeforeSendingCommands() {
        runBlocking {
            val repository = RecordingProChatsListRepository()
            val interactor = DefaultProChatsListInteractor(
                commonInteractor = StubCommonChatsListInteractor(),
                listRepository = repository
            )

            val session = interactor.requestChat()

            assertEquals(expected = "session-id", actual = session.sessionId)

            assertEquals(
                expected = listOf(
                    "listenIncoming",
                    "register",
                    "listenSession",
                    "acceptChat:user_77"
                ),
                actual = repository.calls
            )
        }
    }
}

private class RecordingProChatsListRepository : ProChatsListRepository {
    val calls = mutableListOf<String>()

    override suspend fun register() {
        calls += "register"
    }

    override suspend fun listenIncoming(): Flow<ProIncoming> {
        calls += "listenIncoming"
        return flowOf(ProIncoming(clientLogin = "user_77"))
    }

    override suspend fun acceptChat(clientLogin: String) {
        calls += "acceptChat:$clientLogin"
    }

    override suspend fun listenSession(): Flow<CommonSessionMessage> {
        calls += "listenSession"
        return flowOf(
            CommonSessionMessage(
                sessionId = "session-id",
                status = CommonSessionStatus.Open,
                participantLogin = "user_77",
                participantName = "user_77",
                participantRole = "USER"
            )
        )
    }
}

private class StubCommonChatsListInteractor : CommonChatsListInteractor {
    override suspend fun connect() = Unit

    override suspend fun getChats(): List<CommonChatSummary> = emptyList()

    override suspend fun listenMessages(): Flow<CommonMessage> = emptyFlow()
}
