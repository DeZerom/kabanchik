package ru.kabanchik.common.network.internal.ws

import io.ktor.client.HttpClient
import kotlinx.coroutines.flow.Flow
import org.hildan.krossbow.stomp.StompClient
import org.hildan.krossbow.stomp.conversions.kxserialization.StompSessionWithKxSerialization
import org.hildan.krossbow.stomp.conversions.kxserialization.convertAndSend
import org.hildan.krossbow.stomp.conversions.kxserialization.json.withJsonConversions
import org.hildan.krossbow.stomp.conversions.kxserialization.subscribe
import org.hildan.krossbow.stomp.sendEmptyMsg
import org.hildan.krossbow.websocket.ktor.KtorWebSocketClient
import ru.kabanchik.client.data.chat.logic.api.ClientMessagesStompSource
import ru.kabanchik.common.data.chat.logic.api.CommonStompSource
import ru.kabanchik.common.data.chatDetails.model.CommonApiMessage
import ru.kabanchik.common.data.chatDetails.model.CommonApiSendMessage
import ru.kabanchik.common.data.chatDetails.model.CommonApiSessionMessage
import ru.kabanchik.common.data.chatDetails.model.CommonApiSystemMessage
import ru.kabanchik.pro.data.chat.logic.api.ProMessagesStompSource
import ru.kabanchik.pro.data.chatDetails.model.ProApiAcceptChat
import ru.kabanchik.pro.data.chatDetails.model.ProApiIncoming

internal class DefaultMessagesStompSource(
    private val httpClient: HttpClient
) : CommonStompSource, ClientMessagesStompSource, ProMessagesStompSource {
    var session: StompSessionWithKxSerialization? = null

    override suspend fun connect(token: String) {
        session = StompClient(
            webSocketClient = KtorWebSocketClient(
                httpClient = httpClient
            )
        ).connect(
            url = "ws://185.102.139.25:8080/ws",
            customStompConnectHeaders = createAuthHeader(token)
        ).withJsonConversions()
    }

    override suspend fun register() {
        requireSession().sendEmptyMsg(destination = "/app/executor.register")
    }

    override suspend fun startChat() {
        requireSession().sendEmptyMsg(destination = "/app/chat.request")
    }

    override suspend fun endChat() {
        requireSession().sendEmptyMsg(destination = "/app/chat.end")
    }

    override suspend fun acceptChat(message: ProApiAcceptChat) {
        requireSession().convertAndSend(
            destination = "/app/chat.accept",
            body = message
        )
    }

    override suspend fun send(message: CommonApiSendMessage) {
        requireSession().convertAndSend(
            destination = "/app/chat.send",
            body = message
        )
    }

    override suspend fun listenSystem(): Flow<CommonApiSystemMessage> {
        return requireSession().subscribe(
            destination = "/user/queue/system",
            deserializer = CommonApiSystemMessage.serializer()
        )
    }

    override suspend fun listenSession(): Flow<CommonApiSessionMessage> {
        return requireSession().subscribe(
            destination = "/user/queue/session",
            deserializer = CommonApiSessionMessage.serializer()
        )
    }

    override suspend fun listenIncoming(): Flow<ProApiIncoming> {
        return requireSession().subscribe(
            destination = "/user/queue/incoming",
            deserializer = ProApiIncoming.serializer()
        )
    }

    override suspend fun listenMessages(): Flow<CommonApiMessage> {
        return requireSession().subscribe(
            destination = "/user/queue/messages",
            deserializer = CommonApiMessage.serializer()
        )
    }

    override suspend fun listenSessionEnd(): Flow<CommonApiMessage> {
        return requireSession().subscribe(
            destination = "/user/queue/session-end",
            deserializer = CommonApiMessage.serializer()
        )
    }

    private fun requireSession(): StompSessionWithKxSerialization {
        return session
            ?: throw IllegalStateException("call session before connect()")
    }
}