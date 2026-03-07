package ru.kabanchik.client.data.chatDetails.logic.api

import kotlinx.coroutines.flow.Flow
import ru.kabanchik.common.data.chatDetails.model.CommonApiMessage
import ru.kabanchik.common.data.chatDetails.model.CommonApiSendMessage
import ru.kabanchik.common.data.chatDetails.model.CommonApiSessionMessage
import ru.kabanchik.common.data.chatDetails.model.CommonApiSystemMessage

interface ClientMessagesStompSource {
    suspend fun connect(token: String)
    suspend fun startChat()
    suspend fun endChat()
    suspend fun send(message: CommonApiSendMessage)
    suspend fun listenSystem(): Flow<CommonApiSystemMessage>
    suspend fun listenSession(): Flow<CommonApiSessionMessage>
    suspend fun listenMessages(): Flow<CommonApiMessage>
    suspend fun listenSessionEnd(): Flow<CommonApiMessage>
}