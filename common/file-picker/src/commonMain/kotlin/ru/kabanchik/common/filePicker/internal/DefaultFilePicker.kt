package ru.kabanchik.common.filePicker.internal

import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.dialogs.FileKitMode
import io.github.vinceglb.filekit.dialogs.FileKitType
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
        val files = FileKit.openFilePicker(
            type = FileKitType.File(AllowedFileExtensions),
            mode = FileKitMode.Multiple(),
        ) ?: return emptyList()

        return files.mapNotNull { file ->
            if (!file.name.hasAllowedFileExtension()) return@mapNotNull null

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

internal fun String.hasAllowedFileExtension(): Boolean {
    val extension = substringAfterLast('.', missingDelimiterValue = "").lowercase()
    return extension in AllowedFileExtensions
}

internal val AllowedFileExtensions = setOf(
    "jpg",
    "jpeg",
    "png",
    "gif",
    "webp",
    "pdf",
    "docx",
    "xlsx",
    "pptx",
    "txt",
    "md",
    "csv",
    "log",
    "zip",
    "mp3",
    "m4a",
    "ogg",
    "wav",
    "mp4",
)
