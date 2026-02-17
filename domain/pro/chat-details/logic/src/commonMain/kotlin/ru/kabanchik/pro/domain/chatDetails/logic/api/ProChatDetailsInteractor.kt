package ru.kabanchik.pro.domain.chatDetails.logic.api

import kotlinx.coroutines.flow.Flow
import ru.kabanchik.pro.domain.chatDetails.model.ProMessage

interface ProChatDetailsInteractor {
    suspend fun initChat()
    suspend fun sendMessage(message: ProMessage)
    suspend fun listenMessages(): Flow<ProMessage>
}