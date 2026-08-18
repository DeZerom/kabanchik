package ru.kabanchik.common.filePicker.internal

import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.dialogs.FileKitMode
import io.github.vinceglb.filekit.dialogs.openFilePicker
import io.github.vinceglb.filekit.mimeType
import io.github.vinceglb.filekit.name
import io.github.vinceglb.filekit.readBytes
import io.github.vinceglb.filekit.size
import ru.kabanchik.common.filePicker.api.FilePicker
import ru.kabanchik.common.filePicker.api.SelectedFile

private const val UnknownContentType = "application/octet-stream"

internal class DefaultFilePicker : FilePicker {
    override suspend fun pickFiles(): List<SelectedFile> {
        val files = FileKit.openFilePicker(mode = FileKitMode.Multiple()) ?: return emptyList()

        return files.map { file ->
            val bytes = file.readBytes()
            SelectedFile(
                fileName = file.name,
                contentType = file.mimeType()?.toString() ?: UnknownContentType,
                size = file.size().takeIf { it >= 0L } ?: bytes.size.toLong(),
                bytes = bytes,
            )
        }
    }
}
