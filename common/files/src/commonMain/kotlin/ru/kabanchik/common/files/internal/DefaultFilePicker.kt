package ru.kabanchik.common.files.internal

import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.dialogs.FileKitMode
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.openFilePicker
import io.github.vinceglb.filekit.mimeType
import io.github.vinceglb.filekit.name
import io.github.vinceglb.filekit.readBytes
import io.github.vinceglb.filekit.size
import ru.kabanchik.common.files.api.FilePicker
import ru.kabanchik.common.files.api.SelectedFile

private const val UnknownContentType = "application/octet-stream"

internal class DefaultFilePicker : FilePicker {
    override suspend fun pickFiles(): List<SelectedFile> {
        val files = FileKit.openFilePicker(
            type = FileKitType.File(AllowedFileExtensions),
            mode = FileKitMode.Multiple(),
        ).orEmpty()

        return files
            .filter { it.name.hasAllowedFileExtension() }
            .map { file ->
                SelectedFile(
                    fileName = file.name,
                    contentType = file.mimeType()?.toString() ?: UnknownContentType,
                    size = file.size(),
                    bytes = file.readBytes(),
                )
            }
    }
}

internal fun String.hasAllowedFileExtension(): Boolean {
    val extension = substringAfterLast('.', missingDelimiterValue = "")
    return extension.isNotEmpty() && AllowedFileExtensions.any { it.equals(extension, ignoreCase = true) }
}

private val AllowedFileExtensions = setOf(
    "jpg",
    "jpeg",
    "png",
    "pdf",
    "docx",
    "txt",
    "zip",
    "m4a",
    "mp4",
)
