package ru.kabanchik.client.domain.logic.chatDetails

import kotlinx.coroutines.runBlocking
import ru.kabanchik.client.domain.logic.chatDetails.internal.DefaultChatDetailsInteractor
import ru.kabanchik.client.domain.model.chatDetails.Message
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DefaultChatDetailInteractorTest {
    @Test
    fun sendShortMessage() {
        val (interactor, repository) = createInteractor()

        val message = Message(authorLogin = "qwe", text = "some message")
        runBlocking {
            interactor.sendMessage(message = message)

            assertTrue { repository.messages.size == 1 }
            assertContains(repository.messages, message)
        }
    }

    @Test
    fun sendLongMessage() {
        val (interactor, repository) = createInteractor()

        val message = Message(authorLogin = "qwe", text = "a".repeat(4097))
        runBlocking {
            interactor.sendMessage(message)

            assertTrue { repository.messages.size == 2}
            assertTrue { repository.messages.any { it.authorLogin == message.authorLogin } }

            val messagesText = repository.messages.joinToString(separator = "") { it.text }
            assertEquals(message.text, messagesText)
        }
    }

    @Test
    fun sendEdgeMessage() {
        val (interactor, repository) = createInteractor()

        val message = Message(authorLogin = "qwe", text = "a".repeat(4096))
        runBlocking {
            interactor.sendMessage(message = message)

            assertTrue { repository.messages.size == 1 }
            assertContains(repository.messages, message)
        }
    }

    private fun createInteractor(): Pair<DefaultChatDetailsInteractor, MockChatDetailsRepository> {
        val repo = MockChatDetailsRepository()
        val interactor = DefaultChatDetailsInteractor(repo)
        return interactor to repo
    }
}