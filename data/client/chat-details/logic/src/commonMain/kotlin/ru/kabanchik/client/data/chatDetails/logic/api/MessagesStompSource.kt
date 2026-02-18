package ru.kabanchik.client.data.chatDetails.logic.api

import kotlinx.coroutines.flow.Flow
import ru.kabanchik.client.data.chatDetails.model.ApiMessage

interface MessagesStompSource {
    suspend fun connect(token: String)
    suspend fun send(message: ApiMessage)
    suspend fun listenMessages(): Flow<ApiMessage>
}