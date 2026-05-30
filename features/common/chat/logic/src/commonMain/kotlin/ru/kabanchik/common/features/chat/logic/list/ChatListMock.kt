package ru.kabanchik.common.features.chat.logic.list

import kabanchik.features.common.chat.logic.generated.resources.Res
import kabanchik.features.common.chat.logic.generated.resources.chat_list_time_date
import kabanchik.features.common.chat.logic.generated.resources.chat_list_time_today
import kabanchik.features.common.chat.logic.generated.resources.chat_list_time_yesterday
import ru.kabanchik.common.feature.chat.model.CommonUiChatItem
import ru.kabanchik.common.tools.extensions.asTextResource

object ChatListMock {
    val chatWithUnread = CommonUiChatItem(
        id = "1",
        title = "Бронь ресторана",
        lastUpdate = Res.string.chat_list_time_today.asTextResource("19:45"),
        lastMessage = "Нужно забронировать стол на 5 вечера",
        hasUnread = true,
        otherPersonName = "Иванов Иван"
    )

    val chatLongFields = chatWithUnread.copy(
        id = "2",
        title = "Нужно забронировать стол на 5 вечера Нужно забронировать стол на 5 вечера Нужно забронировать",
        lastUpdate = Res.string.chat_list_time_yesterday.asTextResource("19:45"),
        lastMessage = "Нужно забронировать стол на 5 вечера Нужно забронировать стол на 5 вечера Нужно забронировать Нужно забронировать стол на 5"
    )

    val chatNoName = chatWithUnread.copy(
        id = "3",
        otherPersonName = null,
        hasUnread = false
    )

    val chatDate = chatNoName.copy(
        id = "4",
        lastUpdate = Res.string.chat_list_time_date.asTextResource("30.05.2026", "14:44")
    )

    val chatsList = listOf(chatWithUnread, chatLongFields, chatNoName, chatDate)
}
