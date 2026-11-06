package ru.kabanchik.common.files.api

import kotlinx.io.Buffer
import kotlin.test.Test
import kotlin.test.assertEquals

class SelectedFileTest {
    @Test
    fun opensContentLazilyAndCreatesFreshSource() {
        var openCount = 0
        val file = selectedFile(
            sourceProvider = {
                openCount++
                Buffer()
            },
        )

        assertEquals(0, openCount)

        file.openSource()
        file.openSource()

        assertEquals(2, openCount)
    }

    @Test
    fun releasesAccessOnlyOnce() {
        var releaseCount = 0
        val file = selectedFile(releaseAccess = { releaseCount++ })

        file.release()
        file.release()

        assertEquals(1, releaseCount)
    }

    private fun selectedFile(
        sourceProvider: () -> kotlinx.io.Source = { Buffer() },
        releaseAccess: () -> Unit = {},
    ): SelectedFile {
        return SelectedFile(
            fileName = "document.pdf",
            contentType = "application/pdf",
            size = 0,
            previewUri = "file:///document.pdf",
            sourceProvider = sourceProvider,
            releaseAccess = releaseAccess,
        )
    }
}
