package ru.kabanchik.common.files.internal

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class DefaultFileOpenerTest {
    @Test
    fun removesPathSeparatorsFromCachedFileName() {
        val result = cachedFileName(
            url = "https://example.com/file",
            fileName = "../../folder\\document.pdf",
        )

        assertFalse(result.contains('/'))
        assertFalse(result.contains('\\'))
        assertTrue(result.endsWith("document.pdf"))
    }

    @Test
    fun suppliesFallbackForBlankFileName() {
        val result = cachedFileName(
            url = "https://example.com/file",
            fileName = "   ",
        )

        assertTrue(result.endsWith("_file"))
    }

    @Test
    fun preservesExtensionWhenFileNameIsTruncated() {
        val result = cachedFileName(
            url = "https://example.com/file",
            fileName = "a".repeat(300) + ".pdf",
        )

        assertTrue(result.endsWith(".pdf"))
    }
}
