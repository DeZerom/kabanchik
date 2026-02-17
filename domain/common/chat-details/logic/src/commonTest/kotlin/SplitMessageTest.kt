import kotlinx.coroutines.runBlocking
import ru.kabanchik.common.domain.chatDetails.logic.api.splitMessage
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SplitMessageTest {
    @Test
    fun sendShortMessage() {
        val message = "some message"
        runBlocking {
            val parts = splitMessage(message)

            assertTrue { parts.size == 1 }
            assertContains(parts.first(), message)
        }
    }

    @Test
    fun sendLongMessage() {
        val message = "a".repeat(4097)
        runBlocking {
            val parts = splitMessage(message)

            assertTrue { parts.size == 2}

            val messagesText = parts.joinToString(separator = "")
            assertEquals(message, messagesText)
        }
    }

    @Test
    fun sendEdgeMessage() {
        val message = "a".repeat(4096)
        runBlocking {
            val parts = splitMessage(message)

            assertTrue { parts.size == 1 }
            assertContains(parts.first(), message)
        }
    }
}