package ru.kabanchik.common.network.internal.ws

import dev.shivathapaa.logger.api.loggerD
import dev.shivathapaa.logger.api.loggerE
import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.hildan.krossbow.stomp.StompClient
import org.hildan.krossbow.stomp.config.HeartBeat
import org.hildan.krossbow.stomp.conversions.kxserialization.StompSessionWithKxSerialization
import org.hildan.krossbow.stomp.conversions.kxserialization.convertAndSend
import org.hildan.krossbow.stomp.conversions.kxserialization.json.withJsonConversions
import org.hildan.krossbow.stomp.conversions.kxserialization.subscribe
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
    private val subscriptionsMutex = Mutex()
    private var subscriptionsScope = createSubscriptionsScope()
    private var systemMessagesFlow: Flow<CommonApiSystemMessage>? = null
    private var errorMessagesFlow: Flow<CommonApiSystemMessage>? = null
    private var sessionMessagesFlow: Flow<CommonApiSessionMessage>? = null
    private var incomingMessagesFlow: Flow<ProApiIncoming>? = null
    private var messagesFlow: Flow<CommonApiMessage>? = null
    private var sessionEndMessagesFlow: Flow<CommonApiMessage>? = null

    override suspend fun connect() {
        resetCachedSubscriptions()
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
        ).connect(url = url).withJsonConversions()
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
        return subscriptionsMutex.withLock {
            systemMessagesFlow ?: requireSession().subscribe(
                destination = "/user/queue/system",
                deserializer = CommonApiSystemMessage.serializer()
            ).catch {
                loggerE("System listening error", it)
            }.onEach {
                loggerD("System listening message: $it")
            }.shareSubscription().also {
                loggerD("Start listening system: /user/queue/system")
                systemMessagesFlow = it
            }
        }
    }

    override suspend fun listenErrors(): Flow<CommonApiSystemMessage> {
        return subscriptionsMutex.withLock {
            errorMessagesFlow ?: requireSession().subscribe(
                destination = "/user/queue/errors",
                deserializer = CommonApiSystemMessage.serializer()
            ).catch {
                loggerE("Errors listening error", it)
            }.onEach {
                loggerD("Errors listening message: $it")
            }.shareSubscription().also {
                loggerD("Start listening errors: /user/queue/errors")
                errorMessagesFlow = it
            }
        }
    }

    override suspend fun listenSession(): Flow<CommonApiSessionMessage> {
        return subscriptionsMutex.withLock {
            sessionMessagesFlow ?: requireSession().subscribe(
                destination = "/user/queue/session",
                deserializer = CommonApiSessionMessage.serializer()
            ).catch {
                loggerE("Session listening error", it)
            }.onEach {
                loggerD("Session listening message: $it")
            }.shareSubscription().also {
                loggerD("Start listening session: /user/queue/session")
                sessionMessagesFlow = it
            }
        }
    }

    override suspend fun listenIncoming(): Flow<ProApiIncoming> {
        return subscriptionsMutex.withLock {
            incomingMessagesFlow ?: requireSession().subscribe(
                destination = "/user/queue/incoming",
                deserializer = ProApiIncoming.serializer()
            ).catch {
                loggerE("Incoming listening error", it)
            }.onEach {
                loggerD("Incoming listening message: $it")
            }.shareSubscription().also {
                loggerD("Start listening incoming: /user/queue/incoming")
                incomingMessagesFlow = it
            }
        }
    }

    override suspend fun listenMessages(): Flow<CommonApiMessage> {
        return subscriptionsMutex.withLock {
            messagesFlow ?: requireSession().subscribe(
                destination = "/user/queue/messages",
                deserializer = CommonApiMessage.serializer()
            ).catch {
                loggerE("Messages listening error", it)
            }.onEach {
                loggerD("Messages listening message: $it")
            }.shareSubscription().also {
                loggerD("Start listening messages: /user/queue/messages")
                messagesFlow = it
            }
        }
    }

    override suspend fun listenSessionEnd(): Flow<CommonApiMessage> {
        return subscriptionsMutex.withLock {
            sessionEndMessagesFlow ?: requireSession().subscribe(
                destination = "/user/queue/session-end",
                deserializer = CommonApiMessage.serializer()
            ).catch {
                loggerE("SessionEnd listening error", it)
            }.onEach {
                loggerD("SessionEnd listening message: $it")
            }.shareSubscription().also {
                loggerD("Start listening session end: /user/queue/session-end")
                sessionEndMessagesFlow = it
            }
        }
    }

    private fun <T> Flow<T>.shareSubscription(): Flow<T> {
        return shareIn(
            scope = subscriptionsScope,
            started = SharingStarted.Eagerly,
            replay = 0
        )
    }

    private fun resetCachedSubscriptions() {
        subscriptionsScope.cancel()
        subscriptionsScope = createSubscriptionsScope()
        systemMessagesFlow = null
        errorMessagesFlow = null
        sessionMessagesFlow = null
        incomingMessagesFlow = null
        messagesFlow = null
        sessionEndMessagesFlow = null
    }

    private fun createSubscriptionsScope(): CoroutineScope {
        return CoroutineScope(SupervisorJob() + Dispatchers.Default)
    }

    private fun requireSession(): StompSessionWithKxSerialization {
        return session
            ?: throw IllegalStateException("call session before connect()")
    }
}
