package ru.kabanchik.pro.data.chat.logic.api

import kotlinx.coroutines.flow.Flow
import ru.kabanchik.common.data.chatDetails.model.CommonApiMessage
import ru.kabanchik.common.data.chatDetails.model.CommonApiReconnectMessage
import ru.kabanchik.common.data.chatDetails.model.CommonApiSendMessage
import ru.kabanchik.common.data.chatDetails.model.CommonApiSessionMessage
import ru.kabanchik.common.data.chatDetails.model.CommonApiSystemMessage
import ru.kabanchik.pro.data.chatDetails.model.ProApiAcceptChat
import ru.kabanchik.pro.data.chatDetails.model.ProApiIncoming

interface ProMessagesStompSource {
    suspend fun connect()
    suspend fun register()
    suspend fun acceptChat(message: ProApiAcceptChat)
    suspend fun reconnect(message: CommonApiReconnectMessage)
    suspend fun endChat()
    suspend fun send(message: CommonApiSendMessage)
    suspend fun listenSystem(): Flow<CommonApiSystemMessage>
    suspend fun listenErrors(): Flow<CommonApiSystemMessage>
    suspend fun listenIncoming(): Flow<ProApiIncoming>
    suspend fun listenSession(): Flow<CommonApiSessionMessage>
    suspend fun listenMessages(): Flow<CommonApiMessage>
    suspend fun listenSessionEnd(): Flow<CommonApiMessage>
}
