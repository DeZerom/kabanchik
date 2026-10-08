package ru.kabanchik.common.feature.chat.model

import ru.kabanchik.common.files.api.SelectedFile
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class CommonPendingFile(
    val file: SelectedFile,
    val id: String = Uuid.random().toString(),
    val attachmentId: String? = null,
)
