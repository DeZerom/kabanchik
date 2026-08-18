package ru.kabanchik.common.feature.chat.model

import ru.kabanchik.common.filePicker.api.SelectedFile

data class CommonPendingFile(
    val file: SelectedFile,
    val attachmentId: String? = null,
)
