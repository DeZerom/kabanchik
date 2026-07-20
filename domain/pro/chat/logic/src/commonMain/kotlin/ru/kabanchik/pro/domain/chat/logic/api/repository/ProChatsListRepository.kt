package ru.kabanchik.pro.domain.chat.logic.api.repository

import kotlinx.coroutines.flow.Flow
import ru.kabanchik.common.chat.model.CommonSessionMessage
import ru.kabanchik.pro.domain.chatDetails.model.ProIncoming

interface ProChatsListRepository {
    suspend fun register()
    suspend fun listenIncoming(): Flow<ProIncoming>
    suspend fun acceptChat(sessionId: String)
    suspend fun listenSession(): Flow<CommonSessionMessage>
}
