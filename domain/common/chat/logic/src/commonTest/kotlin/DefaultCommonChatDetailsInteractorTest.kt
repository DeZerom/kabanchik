import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import mock.MockCommonChatDetailsRepository
import mock.MockData
import ru.kabanchik.common.chat.model.CommonChatMessage
import ru.kabanchik.common.chat.model.CommonMessage
import ru.kabanchik.common.domain.chat.logic.internal.DefaultCommonChatDetailsInteractor
import kotlin.test.Test
import kotlin.test.assertEquals

class DefaultCommonChatDetailsInteractorTest {
    @Test
    fun reconnectsToRequestedSession() {
        runBlocking {
            val repository = MockCommonChatDetailsRepository(messages = emptyList())
            val interactor = DefaultCommonChatDetailsInteractor(detailsRepository = repository)

            interactor.reconnect(sessionId = "session-id")

            assertEquals("session-id", repository.reconnectedSessionId)
        }
    }

    @Test
    fun checkOneDayMessagesDates() {
        runBlocking {
            val interactor = createInteractor(MockData.Messages.oneDayMessages)
            val data = mutableListOf<CommonChatMessage>()
            interactor.listenMessages(sessionId = "session").toList(data)

            val dates = data.filterIsInstance<CommonChatMessage.Date>()
            assertEquals(1, dates.size)
            assertEquals(MockData.Messages.message1.time.date, dates.first().date)
        }
    }

    @Test
    fun checkManyDaysMessagesDates() {
        runBlocking {
            val interactor = createInteractor(MockData.Messages.allMessages)
            val data = mutableListOf<CommonChatMessage>()
            interactor.listenMessages(sessionId = "session").toList(data)

            val dates = data.filterIsInstance<CommonChatMessage.Date>()
            assertEquals(2, dates.size)
            assertEquals(MockData.Messages.message1.time.date, dates[0].date)
            assertEquals(MockData.Messages.message3.time.date, dates[1].date)
        }
    }

    @Test
    fun filtersMessagesBySessionId() {
        runBlocking {
            val foreignMessage = MockData.Messages.message2.copy(
                id = "foreign",
                sessionId = "another-session"
            )
            val interactor = createInteractor(
                listOf(MockData.Messages.message1, foreignMessage)
            )
            val data = mutableListOf<CommonChatMessage>()

            interactor.listenMessages(sessionId = "session").toList(data)

            val messages = data.filterIsInstance<CommonChatMessage.Message>()
            assertEquals(1, messages.size)
            assertEquals(MockData.Messages.message1, messages.first().message)
        }
    }

    private fun createInteractor(messages: List<CommonMessage>): DefaultCommonChatDetailsInteractor {
        val repo = MockCommonChatDetailsRepository(
            messages = messages
        )
        return DefaultCommonChatDetailsInteractor(
            detailsRepository = repo
        )
    }
}
