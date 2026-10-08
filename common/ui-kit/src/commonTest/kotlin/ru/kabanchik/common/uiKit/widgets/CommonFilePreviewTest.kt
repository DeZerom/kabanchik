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

    @Test
    fun formatsFileSizeUsingBinaryUnits() {
        assertEquals(
            expected = FormattedFileSize("3,12", FileSizeUnit.Megabytes),
            actual = formatFileSizeValue(3_270_246),
        )
    }

    @Test
    fun coercesNegativeFileSizeToZeroBytes() {
        assertEquals(
            expected = FormattedFileSize("0", FileSizeUnit.Bytes),
            actual = formatFileSizeValue(-1),
        )
    }
}
