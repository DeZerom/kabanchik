package ru.kabanchik.common.feature.chat.model

import ru.kabanchik.common.tools.textResource.TextResource

data class CommonUiChatItem(
    val id: String = "",
    val title: String = "",
    val lastUpdate: TextResource = TextResource.Raw(""),
    val lastMessage: String = "",
    val hasUnread: Boolean = false,
    val otherPersonName: String? = null
)
