package ru.kabanchik.common.network.internal

import io.ktor.client.HttpClient
import kotlinx.coroutines.flow.Flow
import org.hildan.krossbow.stomp.StompClient
import org.hildan.krossbow.stomp.conversions.kxserialization.StompSessionWithKxSerialization
import org.hildan.krossbow.stomp.conversions.kxserialization.convertAndSend
import org.hildan.krossbow.stomp.conversions.kxserialization.json.withJsonConversions
import org.hildan.krossbow.stomp.conversions.kxserialization.subscribe
import org.hildan.krossbow.websocket.ktor.KtorWebSocketClient
import ru.kabanchik.client.data.chatDetails.logic.api.MessagesStompSource
import ru.kabanchik.client.data.chatDetails.model.ApiMessage

class DefaultMessagesStompSource(
    private val httpClient: HttpClient
) : MessagesStompSource {
    var session: StompSessionWithKxSerialization? = null

    override suspend fun connect() {
        session = StompClient(
            webSocketClient = KtorWebSocketClient(
                httpClient = httpClient
            )
        ).connect(
            url = "ws://185.102.139.25:8080/ws",
        ).withJsonConversions()
    }

    override suspend fun send(message: ApiMessage) {
        session?.convertAndSend(
            destination = "/app/chat",
            body = message
        )
    }

    override suspend fun listenMessages(): Flow<ApiMessage> {
        val s = session
            ?: throw IllegalStateException("call DefaultMessagesStompSource.listenMessages before DefaultMessagesStompSource.connect")
        return s.subscribe(
            destination = "/topic/messages",
            deserializer = ApiMessage.serializer()
        )
    }
}