package ru.kabanchik.pro.domain.chat.logic.api

import kotlinx.coroutines.flow.Flow
import ru.kabanchik.common.chat.model.CommonChatMessage

interface ProChatDetailsInteractor {
    suspend fun sendMessage(message: String)
    suspend fun listenMessages(): Flow<CommonChatMessage>
    suspend fun endChat()
}