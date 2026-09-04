package ru.kabanchik.common.network.internal.api.chat

import io.ktor.http.HttpHeaders
import io.ktor.http.content.PartData
import io.ktor.utils.io.InternalAPI
import kotlinx.io.Buffer
import kotlinx.io.RawSource
import kotlinx.io.buffered
import kotlinx.io.readByteArray
import ru.kabanchik.common.files.api.ReadableFile
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(InternalAPI::class)
class FileUploadBodyTest {
    @Test
    fun opensFreshSourceOnlyWhenMultipartPartIsRead() {
        val content = byteArrayOf(1, 2, 3, 4)
        val file = TrackingReadableFile(content)
        val body = createFileUploadBody(
            fileName = "photo.jpg",
            contentType = "image/jpeg",
            file = file,
        )

        assertEquals(0, file.openCount)
        val part = body.parts.single() as PartData.BinaryItem
        assertEquals(content.size.toString(), part.headers[HttpHeaders.ContentLength])
        assertEquals("image/jpeg", part.headers[HttpHeaders.ContentType])
        assertTrue(
            part.headers.getAll(HttpHeaders.ContentDisposition)
                .orEmpty()
                .any { it.contains("filename") && it.contains("photo.jpg") }
        )

        repeat(2) {
            val uploaded = part.provider().use { it.readByteArray() }
            assertContentEquals(content, uploaded)
        }

        assertEquals(2, file.openCount)
        assertEquals(2, file.closeCount)
    }

    private class TrackingReadableFile(
        private val content: ByteArray,
    ) : ReadableFile {
        override val size: Long = content.size.toLong()
        var openCount = 0
        var closeCount = 0

        override fun openSource() = TrackingRawSource(content) { closeCount++ }.buffered().also {
            openCount++
        }
    }

    private class TrackingRawSource(
        content: ByteArray,
        private val onClose: () -> Unit,
    ) : RawSource {
        private val buffer = Buffer().apply { write(content) }

        override fun readAtMostTo(sink: Buffer, byteCount: Long): Long {
            return buffer.readAtMostTo(sink, byteCount)
        }

        override fun close() {
            buffer.close()
            onClose()
        }
    }
}
