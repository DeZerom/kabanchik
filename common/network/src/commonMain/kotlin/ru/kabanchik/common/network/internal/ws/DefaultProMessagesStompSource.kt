package ru.kabanchik.common.network.internal.ws

import io.ktor.client.HttpClient
import kotlinx.coroutines.flow.Flow
import org.hildan.krossbow.stomp.StompClient
import org.hildan.krossbow.stomp.conversions.kxserialization.StompSessionWithKxSerialization
import org.hildan.krossbow.stomp.conversions.kxserialization.convertAndSend
import org.hildan.krossbow.stomp.conversions.kxserialization.json.withJsonConversions
import org.hildan.krossbow.stomp.conversions.kxserialization.subscribe
import org.hildan.krossbow.websocket.ktor.KtorWebSocketClient
import ru.kabanchik.pro.data.chatDetails.logic.api.ProMessagesStompSource
import ru.kabanchik.pro.data.chatDetails.model.ApiProMessage

internal class DefaultProMessagesStompSource(
    private val httpClient: HttpClient
) : ProMessagesStompSource {
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

    override suspend fun send(message: ApiProMessage) {
        session?.convertAndSend(
            destination = "/app/chat",
            body = message
        )
    }

    override suspend fun listenMessages(): Flow<ApiProMessage> {
        val s = session
            ?: throw IllegalStateException("call DefaultMessagesStompSource.listenMessages before DefaultMessagesStompSource.connect")
        return s.subscribe(
            destination = "/topic/messages",
            deserializer = ApiProMessage.serializer()
        )
    }
}