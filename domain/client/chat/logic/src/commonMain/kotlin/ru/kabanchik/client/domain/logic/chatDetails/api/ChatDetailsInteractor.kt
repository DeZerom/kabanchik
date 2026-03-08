package ru.kabanchik.client.domain.logic.chatDetails.api

import kotlinx.coroutines.flow.Flow
import ru.kabanchik.common.chat.model.CommonMessage

interface ChatDetailsInteractor {
    suspend fun initChat()
    suspend fun sendMessage(message: CommonMessage)
    suspend fun listenMessages(): Flow<CommonMessage>
}