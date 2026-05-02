package ru.kabanchik.common.feature.chat.model

data class CommonUiChatItem(
    val id: String = "",
    val title: String = "",
    val lastUpdate: String = "",
    val lastMessage: String = "",
    val hasUnread: Boolean = false,
    val otherPersonName: String? = null
)
