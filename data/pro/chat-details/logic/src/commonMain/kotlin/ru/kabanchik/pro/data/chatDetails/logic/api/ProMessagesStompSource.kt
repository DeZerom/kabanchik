package ru.kabanchik.pro.data.chatDetails.logic.api

import kotlinx.coroutines.flow.Flow
import ru.kabanchik.pro.data.chatDetails.model.ApiProMessage

interface ProMessagesStompSource {
    suspend fun connect(token: String)
    suspend fun send(message: ApiProMessage)
    suspend fun listenMessages(): Flow<ApiProMessage>
}