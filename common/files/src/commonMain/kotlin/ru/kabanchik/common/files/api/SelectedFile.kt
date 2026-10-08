package ru.kabanchik.common.files.api

import kotlinx.io.Source

interface ReadableFile {
    val size: Long

    fun openSource(): Source
}

class SelectedFile(
    val fileName: String,
    val contentType: String,
    override val size: Long,
    val previewUri: String,
    private val sourceProvider: () -> Source,
    private val releaseAccess: () -> Unit = {},
) : ReadableFile {
    private var isReleased = false

    override fun openSource(): Source = sourceProvider()

    fun release() {
        if (isReleased) return
        isReleased = true
        releaseAccess()
    }
}
