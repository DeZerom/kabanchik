package ru.kabanchik.common.domain.chat.logic.api

import ru.kabanchik.common.chat.model.CommonChatSummary

interface CommonChatsListInteractor {
    suspend fun connect()
    suspend fun getChats(): List<CommonChatSummary>
}
