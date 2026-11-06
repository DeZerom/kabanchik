import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.LocalDateTime
import mock.MockCommonChatDetailsRepository
import ru.kabanchik.common.chat.model.CommonChatMessage
import ru.kabanchik.common.chat.model.CommonChatSummary
import ru.kabanchik.common.chat.model.CommonMessage
import ru.kabanchik.common.chat.model.CommonMessageType
import ru.kabanchik.common.chat.model.CommonSessionMessage
import ru.kabanchik.common.chat.model.CommonSessionStatus
import ru.kabanchik.common.domain.chat.logic.api.repository.CommonChatsListRepository
import ru.kabanchik.common.domain.chat.logic.internal.DefaultCommonChatDetailsInteractor
import ru.kabanchik.common.domain.chat.logic.internal.DefaultCommonChatsListInteractor
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class CommonChatClosingTest {
    @Test
    fun mapsSystemMessageFromHistoryToSessionEnd() {
        runBlocking {
            val textMessage = message(id = "text", type = CommonMessageType.Text)
            val systemMessage = message(id = "system", type = CommonMessageType.System)
            val interactor = DefaultCommonChatDetailsInteractor(
                detailsRepository = MockCommonChatDetailsRepository(messages = listOf(textMessage, systemMessage))
            )

            val messages = interactor.getMessages(sessionId = SessionId)

            assertIs<CommonChatMessage.Date>(messages[0])
            assertEquals(CommonChatMessage.Message(textMessage), messages[1])
            assertEquals(CommonChatMessage.SessionEnd(systemMessage), messages[2])
        }
    }

    @Test
    fun emitsSessionEndFromLiveEvent() {
        runBlocking {
            val systemMessage = message(id = "system", type = CommonMessageType.System)
            val interactor = DefaultCommonChatDetailsInteractor(
                detailsRepository = MockCommonChatDetailsRepository(
                    messages = emptyList(),
                    sessionEnds = flowOf(systemMessage, systemMessage.copy(sessionId = OtherSessionId)),
                )
            )

            val messages = interactor.listenMessages(sessionId = SessionId).toList()

            assertEquals(
                listOf(
                    CommonChatMessage.Date(systemMessage.time.date),
                    CommonChatMessage.SessionEnd(systemMessage),
                ),
                messages
            )
        }
    }

    @Test
    fun listensClosingOfRequestedSessionOnly() {
        runBlocking {
            val interactor = DefaultCommonChatDetailsInteractor(
                detailsRepository = MockCommonChatDetailsRepository(
                    messages = emptyList(),
                    sessions = flowOf(
                        CommonSessionMessage(sessionId = SessionId, status = CommonSessionStatus.Open),
                        CommonSessionMessage(sessionId = OtherSessionId, status = CommonSessionStatus.Closed),
                        CommonSessionMessage(sessionId = SessionId, status = CommonSessionStatus.Closed),
                    ),
                    sessionEnds = flowOf(
                        message(id = "other", type = CommonMessageType.System, sessionId = OtherSessionId),
                        message(id = "system", type = CommonMessageType.System),
                    ),
                )
            )

            val closings = interactor.listenClosed(sessionId = SessionId).toList()

            // Одно закрытие из /queue/session и одно из /queue/session-end
            assertEquals(2, closings.size)
        }
    }

    @Test
    fun listensClosedChatsOnly() {
        runBlocking {
            val interactor = DefaultCommonChatsListInteractor(
                listRepository = FakeCommonChatsListRepository(
                    sessions = flowOf(
                        CommonSessionMessage(sessionId = "paused", status = CommonSessionStatus.Paused),
                        CommonSessionMessage(sessionId = "closed", status = CommonSessionStatus.Closed),
                        CommonSessionMessage(sessionId = "open", status = CommonSessionStatus.Open),
                    )
                )
            )

            assertEquals(listOf("closed"), interactor.listenClosedChats().toList())
        }
    }

    @Test
    fun keepsServerOrderOfClosedChats() {
        runBlocking {
            val closedChats = listOf(
                summary(sessionId = "newer", lastMessageTime = LocalDateTime(2026, 11, 1, 10, 0)),
                summary(sessionId = "older", lastMessageTime = LocalDateTime(2026, 11, 5, 10, 0)),
            )
            val interactor = DefaultCommonChatsListInteractor(
                listRepository = FakeCommonChatsListRepository(closedChats = closedChats)
            )

            assertEquals(listOf("newer", "older"), interactor.getClosedChats().map { it.sessionId })
        }
    }

    private fun message(
        id: String,
        type: CommonMessageType,
        sessionId: String = SessionId,
    ): CommonMessage = CommonMessage(
        id = id,
        sessionId = sessionId,
        authorLogin = if (type == CommonMessageType.System) "SYSTEM" else "user",
        text = "Message",
        type = type,
        time = LocalDateTime(2026, 11, 5, 10, 0),
    )

    private fun summary(sessionId: String, lastMessageTime: LocalDateTime): CommonChatSummary = CommonChatSummary(
        sessionId = sessionId,
        status = CommonSessionStatus.Closed,
        participantName = null,
        firstMessageContent = null,
        lastMessageContent = null,
        lastMessageTimestamp = lastMessageTime,
        lastMessageSender = null,
    )

    private class FakeCommonChatsListRepository(
        private val sessions: Flow<CommonSessionMessage> = emptyFlow(),
        private val closedChats: List<CommonChatSummary> = emptyList(),
    ) : CommonChatsListRepository {
        override suspend fun connect() = Unit

        override suspend fun listenReconnections(): Flow<Unit> = emptyFlow()

        override suspend fun getChats(): List<CommonChatSummary> = emptyList()

        override suspend fun getClosedChats(): List<CommonChatSummary> = closedChats

        override suspend fun listenMessages(): Flow<CommonMessage> = emptyFlow()

        override suspend fun listenSession(): Flow<CommonSessionMessage> = sessions
    }

    private companion object {
        const val SessionId = "session-1"
        const val OtherSessionId = "session-2"
    }
}
