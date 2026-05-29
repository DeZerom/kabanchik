package ru.kabanchik.pro.domain.chat.logic.api

import ru.kabanchik.common.chat.model.CommonChatSummary

interface ProChatsListInteractor {
    suspend fun connect()
    suspend fun getChats(): List<CommonChatSummary>
    suspend fun requestChat()
}
