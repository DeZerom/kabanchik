package ru.kabanchik.common.feature.chat.model

import ru.kabanchik.common.tools.textResource.TextResource
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
sealed interface CommonUiMessage {
    val id: String

    data class Date(
        val date: String,
        override val id: String = Uuid.random().toString()
    ) : CommonUiMessage
    data class SystemMessage(
        val message: TextResource,
        override val id: String = Uuid.random().toString()
    ) : CommonUiMessage
    data class Message(
        override val id: String = "",
        val authorLogin: String = "",
        val isUserAuthor: Boolean = false,
        val time: String = "",
        val text: String = "",
        val attachments: List<CommonUiAttachment> = emptyList(),
    ) : CommonUiMessage
}
