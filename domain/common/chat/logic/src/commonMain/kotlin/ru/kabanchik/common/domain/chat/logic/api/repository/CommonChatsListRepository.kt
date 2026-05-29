package ru.kabanchik.common.domain.chat.logic.api.repository

import ru.kabanchik.common.chat.model.CommonChatSummary

interface CommonChatsListRepository {
    suspend fun connect(token: String)
    suspend fun getChats(): List<CommonChatSummary>
}
