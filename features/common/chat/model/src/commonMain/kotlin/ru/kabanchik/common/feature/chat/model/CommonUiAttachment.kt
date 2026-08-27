package ru.kabanchik.common.feature.chat.model

sealed interface CommonUiAttachment {
    val fileId: String
    val originalName: String
    val contentType: String
    val size: Long
    val downloadUrl: String

    data class Image(
        override val fileId: String,
        override val originalName: String,
        override val contentType: String,
        override val size: Long,
        override val downloadUrl: String,
    ) : CommonUiAttachment

    data class File(
        override val fileId: String,
        override val originalName: String,
        override val contentType: String,
        override val size: Long,
        override val downloadUrl: String,
        val isLoading: Boolean = false,
    ) : CommonUiAttachment
}
