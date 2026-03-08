package ru.kabanchik.pro.domain.chatDetails.logic.api.repository

import kotlinx.coroutines.flow.Flow
import ru.kabanchik.common.chat.model.CommonMessage
import ru.kabanchik.common.chat.model.CommonSessionMessage
import ru.kabanchik.pro.domain.chatDetails.model.ProIncoming

interface ProChatDetailsRepository {
    suspend fun connect(token: String)
    suspend fun listenIncoming(): Flow<ProIncoming>
    suspend fun acceptChat(clientLogin: String)
    suspend fun listenSession(): Flow<CommonSessionMessage>
    suspend fun sendMessage(message: String)
    suspend fun listenMessages(): Flow<CommonMessage>
    suspend fun endChat()
    suspend fun listenSessionEnd(): Flow<CommonMessage>
}