package ru.kabanchik.common.features.chat.logic.details

import kotlinx.io.Buffer
import ru.kabanchik.common.feature.chat.model.CommonPendingFile
import ru.kabanchik.common.files.api.SelectedFile
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame
import kotlin.test.assertTrue

class CommonPendingFilesTest {
    @Test
    fun appendsFilesToExistingSelection() {
        val first = selectedFile("first.pdf")
        val second = selectedFile("second.pdf")

        val result = listOf(CommonPendingFile(id = "first-id", file = first))
            .appendSelectedFiles(listOf(second))

        assertEquals(2, result.size)
        assertSame(first, result[0].file)
        assertSame(second, result[1].file)
        assertTrue(result[1].id.isNotBlank())
        assertTrue(result[0].id != result[1].id)
    }

    @Test
    fun removesOnlyRequestedFileWhenNamesAreEqual() {
        val first = selectedFile("document.pdf")
        val second = selectedFile("document.pdf")

        val result = listOf(
            CommonPendingFile(id = "first-id", file = first),
            CommonPendingFile(id = "second-id", file = second),
        ).removeSelectedFile(id = "first-id")

        assertEquals(1, result.size)
        assertSame(second, result.single().file)
    }

    private fun selectedFile(fileName: String): SelectedFile {
        return SelectedFile(
            fileName = fileName,
            contentType = "application/pdf",
            size = 1,
            previewUri = "file:///$fileName",
            sourceProvider = { Buffer() },
        )
    }
}
