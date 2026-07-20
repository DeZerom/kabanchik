package ru.kabanchik.client.data.chat.logic.api

import kotlinx.coroutines.flow.Flow
import ru.kabanchik.client.data.chatDetails.model.ClientApiChatRequest
import ru.kabanchik.common.data.chatDetails.model.CommonApiErrorMessage
import ru.kabanchik.common.data.chatDetails.model.CommonApiMessage
import ru.kabanchik.common.data.chatDetails.model.CommonApiReconnectMessage
import ru.kabanchik.common.data.chatDetails.model.CommonApiSendMessage
import ru.kabanchik.common.data.chatDetails.model.CommonApiSessionMessage
import ru.kabanchik.common.data.chatDetails.model.CommonApiSystemMessage

interface ClientMessagesStompSource {
    suspend fun connect()
    suspend fun startChat(message: ClientApiChatRequest)
    suspend fun reconnect(message: CommonApiReconnectMessage)
    suspend fun endChat(sessionId: String)
    suspend fun send(message: CommonApiSendMessage)
    suspend fun listenSystem(): Flow<CommonApiSystemMessage>
    suspend fun listenErrors(): Flow<CommonApiErrorMessage>
    suspend fun listenSession(): Flow<CommonApiSessionMessage>
    suspend fun listenMessages(): Flow<CommonApiMessage>
    suspend fun listenSessionEnd(): Flow<CommonApiMessage>
}
