package ru.kabanchik.common.features.chat.logic.list

import ru.kabanchik.common.feature.chat.model.CommonUiChatItem

object ChatListMock {
    val chatWithUnread = CommonUiChatItem(
        id = "1",
        title = "Бронь ресторана",
        lastUpdate = "19:45",
        lastMessage = "Нужно забронировать стол на 5 вечера",
        hasUnread = true,
        otherPersonName = "Иванов Иван"
    )

    val chatLongFields = chatWithUnread.copy(
        id = "2",
        title = "Нужно забронировать стол на 5 вечера Нужно забронировать стол на 5 вечера Нужно забронировать",
        lastUpdate = "вчера в 19:45",
        lastMessage = "Нужно забронировать стол на 5 вечера Нужно забронировать стол на 5 вечера Нужно забронировать Нужно забронировать стол на 5"
    )

    val chatNoName = chatWithUnread.copy(
        id = "3",
        otherPersonName = null,
        hasUnread = false
    )

    val chatsList = listOf(chatWithUnread, chatLongFields, chatNoName)
}