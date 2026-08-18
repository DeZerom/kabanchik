import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import mock.MockCommonChatDetailsRepository
import mock.MockData
import ru.kabanchik.common.chat.model.CommonChatMessage
import ru.kabanchik.common.chat.model.CommonMessage
import ru.kabanchik.common.domain.chat.logic.internal.DefaultCommonChatDetailsInteractor
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertContentEquals

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
    fun getsMessagesForRequestedSession() {
        runBlocking {
            val repository = MockCommonChatDetailsRepository(
                messages = MockData.Messages.allMessages
            )
            val interactor = DefaultCommonChatDetailsInteractor(detailsRepository = repository)

            val data = interactor.getMessages(sessionId = "session")

            val messages = data.filterIsInstance<CommonChatMessage.Message>()
            val dates = data.filterIsInstance<CommonChatMessage.Date>()
            assertEquals("session", repository.requestedMessagesSessionId)
            assertEquals(MockData.Messages.allMessages, messages.map { it.message })
            assertEquals(listOf(MockData.Messages.message1.time.date, MockData.Messages.message3.time.date), dates.map { it.date })
        }
    }

    @Test
    fun generatesNewClientMessageIdForEachMessage() {
        runBlocking {
            val repository = MockCommonChatDetailsRepository(messages = emptyList())
            val interactor = DefaultCommonChatDetailsInteractor(detailsRepository = repository)

            interactor.sendMessage(
                sessionId = "session-id",
                content = "First",
                attachmentIds = emptyList(),
            )
            interactor.sendMessage(
                sessionId = "session-id",
                content = "Second",
                attachmentIds = emptyList(),
            )

            val firstId = repository.sentMessages[0].clientMessageId
            val secondId = repository.sentMessages[1].clientMessageId
            assertNotEquals(firstId, secondId)
        }
    }

    @Test
    fun uploadsFileForRequestedSession() {
        runBlocking {
            val repository = MockCommonChatDetailsRepository(messages = emptyList())
            val interactor = DefaultCommonChatDetailsInteractor(detailsRepository = repository)
            val bytes = byteArrayOf(4, 5, 6)

            val attachment = interactor.uploadFile(
                sessionId = "session-id",
                fileName = "document.pdf",
                contentType = "application/pdf",
                bytes = bytes,
            )

            val request = repository.uploadedFileRequest
            assertEquals("session-id", request?.sessionId)
            assertEquals("document.pdf", request?.fileName)
            assertEquals("application/pdf", request?.contentType)
            assertContentEquals(bytes, request?.bytes)
            assertEquals("uploaded-file-id", attachment.fileId)
        }
    }

    @Test
    fun downloadsFileForRequestedSession() {
        runBlocking {
            val repository = MockCommonChatDetailsRepository(messages = emptyList())
            val interactor = DefaultCommonChatDetailsInteractor(detailsRepository = repository)

            val bytes = interactor.downloadFile(sessionId = "session-id", fileId = "file-id")

            assertEquals("session-id", repository.downloadedFileRequest?.sessionId)
            assertEquals("file-id", repository.downloadedFileRequest?.fileId)
            assertContentEquals(byteArrayOf(1, 2, 3), bytes)
        }
    }

    @Test
    fun sendsAttachmentsWithoutText() {
        runBlocking {
            val repository = MockCommonChatDetailsRepository(messages = emptyList())
            val interactor = DefaultCommonChatDetailsInteractor(detailsRepository = repository)

            interactor.sendMessage(
                sessionId = "session-id",
                content = null,
                attachmentIds = listOf("file-id"),
            )

            assertEquals(null, repository.sentMessages.single().content)
            assertEquals(listOf("file-id"), repository.sentMessages.single().attachmentIds)
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
