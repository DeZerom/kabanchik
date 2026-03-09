package ru.kabanchik.pro.domain.chat.logic.api

import kotlinx.coroutines.flow.Flow
import ru.kabanchik.common.chat.model.CommonMessage

interface ProChatDetailsInteractor {
    suspend fun initChat()
    suspend fun sendMessage(message: String)
    suspend fun listenMessages(): Flow<CommonMessage>
}