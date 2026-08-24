package ru.kabanchik.common.features.chat.logic.details

import ru.kabanchik.common.feature.chat.model.CommonPendingFile
import ru.kabanchik.common.filePicker.api.SelectedFile

fun List<CommonPendingFile>.appendSelectedFiles(
    files: List<SelectedFile>,
): List<CommonPendingFile> {
    return this + files.map { file ->
        CommonPendingFile(file = file)
    }
}

fun List<CommonPendingFile>.removeSelectedFile(
    id: String,
): List<CommonPendingFile> {
    return filterNot { it.id == id }
}
