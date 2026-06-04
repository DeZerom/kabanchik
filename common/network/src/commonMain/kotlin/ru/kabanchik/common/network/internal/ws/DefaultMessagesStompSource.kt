package ru.kabanchik.common.network.internal.ws

import dev.shivathapaa.logger.api.loggerD
import dev.shivathapaa.logger.api.loggerE
import io.ktor.client.HttpClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onEach
import org.hildan.krossbow.stomp.StompClient
import org.hildan.krossbow.stomp.conversions.kxserialization.StompSessionWithKxSerialization
import org.hildan.krossbow.stomp.conversions.kxserialization.convertAndSend
import org.hildan.krossbow.stomp.conversions.kxserialization.json.withJsonConversions
import org.hildan.krossbow.stomp.conversions.kxserialization.subscribe
import org.hildan.krossbow.stomp.config.HeartBeat
import org.hildan.krossbow.stomp.sendEmptyMsg
import org.hildan.krossbow.websocket.ktor.KtorWebSocketClient
import ru.kabanchik.client.data.chat.logic.api.ClientMessagesStompSource
import ru.kabanchik.common.data.chat.logic.api.CommonStompSource
import ru.kabanchik.common.data.chatDetails.model.CommonApiMessage
import ru.kabanchik.common.data.chatDetails.model.CommonApiReconnectMessage
import ru.kabanchik.common.data.chatDetails.model.CommonApiSendMessage
import ru.kabanchik.common.data.chatDetails.model.CommonApiSessionMessage
import ru.kabanchik.common.data.chatDetails.model.CommonApiSystemMessage
import ru.kabanchik.common.network.internal.DEFAULT_HOST
import ru.kabanchik.pro.data.chat.logic.api.ProMessagesStompSource
import ru.kabanchik.pro.data.chatDetails.model.ProApiAcceptChat
import ru.kabanchik.pro.data.chatDetails.model.ProApiIncoming
import kotlin.time.Duration.Companion.seconds

internal class DefaultMessagesStompSource(
    private val httpClient: HttpClient
) : CommonStompSource, ClientMessagesStompSource, ProMessagesStompSource {
    var session: StompSessionWithKxSerialization? = null

    override suspend fun connect(token: String) {
        val url = "ws://$DEFAULT_HOST/ws"
        loggerD("Connect: $url")
        session = StompClient(
            webSocketClient = KtorWebSocketClient(
                httpClient = httpClient
            ),
            configure = {
                heartBeat = HeartBeat(
                    minSendPeriod = 5.seconds,
                    expectedPeriod = 5.seconds
                )
            }
        ).connect(
            url = url,
            customStompConnectHeaders = createStompHeaders(token)
        ).withJsonConversions()
    }

    override suspend fun register() {
        loggerD("Register: app/executor.register")
        requireSession().sendEmptyMsg(destination = "/app/executor.register")
    }

    override suspend fun startChat() {
        loggerD("Start chat: /app/chat.request")
        requireSession().sendEmptyMsg(destination = "/app/chat.request")
    }

    override suspend fun endChat() {
        loggerD("End chat: /app/chat.end")
        requireSession().sendEmptyMsg(destination = "/app/chat.end")
    }

    override suspend fun reconnect(message: CommonApiReconnectMessage) {
        loggerD("Reconnect chat: /app/chat.reconnect. Body: $message")
        requireSession().convertAndSend(
            destination = "/app/chat.reconnect",
            body = message
        )
    }

    override suspend fun acceptChat(message: ProApiAcceptChat) {
        loggerD("Accept chat: /app/chat.accept. Body: $message")
        requireSession().convertAndSend(
            destination = "/app/chat.accept",
            body = message
        )
    }

    override suspend fun send(message: CommonApiSendMessage) {
        loggerD("Send message: /app/chat.send. Body: $message")
        requireSession().convertAndSend(
            destination = "/app/chat.send",
            body = message
        )
    }

    override suspend fun listenSystem(): Flow<CommonApiSystemMessage> {
        loggerD("Start listening system: /user/queue/system")
        return requireSession().subscribe(
            destination = "/user/queue/system",
            deserializer = CommonApiSystemMessage.serializer()
        ).catch {
            loggerE("System listening error", it)
        }.onEach {
            loggerD("System listening message: $it")
        }
    }

    override suspend fun listenErrors(): Flow<CommonApiSystemMessage> {
        loggerD("Start listening errors: /user/queue/errors")
        return requireSession().subscribe(
            destination = "/user/queue/errors",
            deserializer = CommonApiSystemMessage.serializer()
        ).catch {
            loggerE("Errors listening error", it)
        }.onEach {
            loggerD("Errors listening message: $it")
        }
    }

    override suspend fun listenSession(): Flow<CommonApiSessionMessage> {
        loggerD("Start listening session: /user/queue/session")
        return requireSession().subscribe(
            destination = "/user/queue/session",
            deserializer = CommonApiSessionMessage.serializer()
        ).catch {
            loggerE("Session listening error", it)
        }.onEach {
            loggerD("Session listening message: $it")
        }
    }

    override suspend fun listenIncoming(): Flow<ProApiIncoming> {
        loggerD("Start listening incoming: /user/queue/incoming")
        return requireSession().subscribe(
            destination = "/user/queue/incoming",
            deserializer = ProApiIncoming.serializer()
        ).catch {
            loggerE("Incoming listening error", it)
        }.onEach {
            loggerD("Incoming listening message: $it")
        }
    }

    override suspend fun listenMessages(): Flow<CommonApiMessage> {
        loggerD("Start listening messages: /user/queue/messages")
        return requireSession().subscribe(
            destination = "/user/queue/messages",
            deserializer = CommonApiMessage.serializer()
        ).catch {
            loggerE("Messages listening error", it)
        }.onEach {
            loggerD("Messages listening message: $it")
        }
    }

    override suspend fun listenSessionEnd(): Flow<CommonApiMessage> {
        loggerD("Start listening session end: /user/queue/messages")
        return requireSession().subscribe(
            destination = "/user/queue/session-end",
            deserializer = CommonApiMessage.serializer()
        ).catch {
            loggerE("SessionEnd listening error", it)
        }.onEach {
            loggerD("SessionEnd listening message: $it")
        }
    }

    private fun requireSession(): StompSessionWithKxSerialization {
        return session
            ?: throw IllegalStateException("call session before connect()")
    }
}
