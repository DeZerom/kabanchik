package ru.kabanchik.common.uiKit.widgets

import kotlin.test.Test
import kotlin.test.assertEquals

class CommonFilePreviewTest {
    @Test
    fun formatsTypeFromMimeType() {
        assertEquals(
            expected = "PDF",
            actual = formatFileType("application/pdf", "document.unknown"),
        )
    }

    @Test
    fun fallsBackToFileExtensionForUnknownMimeType() {
        assertEquals(
            expected = "LOG",
            actual = formatFileType("application/octet-stream", "application.log"),
        )
    }

    @Test
    fun formatsUnknownTypeWhenMimeAndExtensionAreMissing() {
        assertEquals(
            expected = "FILE",
            actual = formatFileType("application/octet-stream", "README"),
        )
    }

    @Test
    fun removesOnlyLastExtensionFromDisplayName() {
        assertEquals(
            expected = "archive.backup",
            actual = formatFileName("archive.backup.zip"),
        )
    }
}
