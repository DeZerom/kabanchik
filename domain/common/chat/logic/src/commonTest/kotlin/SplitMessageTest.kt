import kotlinx.coroutines.runBlocking
import ru.kabanchik.common.domain.chatDetails.logic.api.splitAndTrimMessage
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SplitMessageTest {
    @Test
    fun sendShortMessage() {
        val message = "some message"
        runBlocking {
            val parts = splitAndTrimMessage(message)

            assertTrue { parts.size == 1 }
            assertContains(parts.first(), message)
        }
    }

    @Test
    fun sendLongMessage() {
        val message = "a".repeat(4097)
        runBlocking {
            val parts = splitAndTrimMessage(message)

            assertTrue { parts.size == 2}

            val messagesText = parts.joinToString(separator = "")
            assertEquals(message, messagesText)
        }
    }

    @Test
    fun sendEdgeMessage() {
        val message = "a".repeat(4096)
        runBlocking {
            val parts = splitAndTrimMessage(message)

            assertTrue { parts.size == 1 }
            assertContains(parts.first(), message)
        }
    }

    @Test
    fun sendWhitespaces() {
        val message = "      a"
        runBlocking {
            val parts = splitAndTrimMessage(message)

            assertTrue { parts.size == 1 }
            assertEquals("a", parts.first())
        }
    }

    @Test
    fun sendBlankLines() {
        val message = "\n\n  a\n\n\n"
        runBlocking {
            val parts = splitAndTrimMessage(message)

            assertTrue { parts.size == 1 }
            assertEquals("a", parts.first())
        }
    }

    @Test
    fun sendBlankLinesInMiddle() {
        val message = "a\nb\n\nc\n\n\nd"
        runBlocking {
            val parts = splitAndTrimMessage(message)

            assertTrue { parts.size == 1 }
            assertEquals(message, parts.first())
        }
    }

    @Test
    fun sendLongMessageWithWhitespaces() {
        val message = StringBuilder()
            .append(" ".repeat(4096))
            .append("a")
            .toString()
        runBlocking {
            val parts = splitAndTrimMessage(message)

            assertTrue { parts.size == 1 }
            assertEquals("a", parts.first())
        }
    }

    @Test
    fun sendLongMessageWithBlankLines() {
        val message = StringBuilder()
            .append("\n")
            .append("a")
            .append("\n".repeat(4096))
            .append("b   ")
            .toString()
        val trimmedMessage = StringBuilder()
            .append("a")
            .append("\n".repeat(4096))
            .append("b")
            .toString()
        runBlocking {
            val parts = splitAndTrimMessage(message)

            assertTrue { parts.size == 2 }
            assertEquals(trimmedMessage, parts.joinToString(separator = ""))
        }
    }
}