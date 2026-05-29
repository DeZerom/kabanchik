package ru.kabanchik.pro.domain.chat.logic.api

import kotlinx.coroutines.flow.Flow
import ru.kabanchik.common.chat.model.CommonChatSummary
import ru.kabanchik.common.chat.model.CommonMessage

interface ProChatsListInteractor {
    suspend fun connect()
    suspend fun getChats(): List<CommonChatSummary>
    suspend fun listenMessages(): Flow<CommonMessage>
    suspend fun requestChat()
}
