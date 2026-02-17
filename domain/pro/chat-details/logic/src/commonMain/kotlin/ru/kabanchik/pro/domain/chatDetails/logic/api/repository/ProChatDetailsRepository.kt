package ru.kabanchik.pro.domain.chatDetails.logic.api.repository

import kotlinx.coroutines.flow.Flow
import ru.kabanchik.pro.domain.chatDetails.model.ProMessage

interface ProChatDetailsRepository {
    suspend fun initChat(token: String)
    suspend fun sendMessage(message: ProMessage)
    suspend fun listenMessages(): Flow<ProMessage>
}