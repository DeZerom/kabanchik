package ru.kabanchik.common.data.chat.logic.api

import kotlinx.coroutines.flow.Flow
import ru.kabanchik.common.data.chatDetails.model.CommonApiMessage
import ru.kabanchik.common.data.chatDetails.model.CommonApiSendMessage
import ru.kabanchik.common.data.chatDetails.model.CommonApiSessionMessage

interface CommonStompSource {
    suspend fun connect(token: String)
    suspend fun send(message: CommonApiSendMessage)
    suspend fun listenSession(): Flow<CommonApiSessionMessage>
    suspend fun listenMessages(): Flow<CommonApiMessage>
    suspend fun endChat()
    suspend fun listenSessionEnd(): Flow<CommonApiMessage>
}