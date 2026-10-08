package ru.kabanchik.common.network.internal.ws

import dev.shivathapaa.logger.api.loggerD
import dev.shivathapaa.logger.api.loggerE
import io.ktor.client.HttpClient
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import org.hildan.krossbow.stomp.StompClient
import org.hildan.krossbow.stomp.config.HeartBeat
import org.hildan.krossbow.stomp.conversions.kxserialization.StompSessionWithKxSerialization
import org.hildan.krossbow.stomp.conversions.kxserialization.convertAndSend
import org.hildan.krossbow.stomp.conversions.kxserialization.json.withJsonConversions
import org.hildan.krossbow.stomp.conversions.kxserialization.subscribe
import org.hildan.krossbow.stomp.sendEmptyMsg
import org.hildan.krossbow.websocket.ktor.KtorWebSocketClient
import ru.kabanchik.client.data.chat.logic.api.ClientMessagesStompSource
import ru.kabanchik.client.data.chatDetails.model.ClientApiChatRequest
import ru.kabanchik.common.data.chat.logic.api.CommonStompSource
import ru.kabanchik.common.data.chatDetails.model.CommonApiEndChat
import ru.kabanchik.common.data.chatDetails.model.CommonApiErrorMessage
import ru.kabanchik.common.data.chatDetails.model.CommonApiMessage
import ru.kabanchik.common.data.chatDetails.model.CommonApiReconnectMessage
import ru.kabanchik.common.data.chatDetails.model.CommonApiSendMessage
import ru.kabanchik.common.data.chatDetails.model.CommonApiSessionMessage
import ru.kabanchik.common.data.chatDetails.model.CommonApiSystemMessage
import ru.kabanchik.common.network.api.StompConnectionController
import ru.kabanchik.common.tools.network.BackendHost
import ru.kabanchik.pro.data.chat.logic.api.ProMessagesStompSource
import ru.kabanchik.pro.data.chatDetails.model.ProApiAcceptChat
import ru.kabanchik.pro.data.chatDetails.model.ProApiIncoming
import kotlin.math.min
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

private val InitialReconnectDelay = 1.seconds
private val MaxReconnectDelay = 30.seconds
private val SendConnectionTimeout = 10.seconds

/**
 * Держит одно STOMP-соединение и автоматически восстанавливает его при обрыве.
 *
 * Подписки регистрируются один раз и переживают переподключения: после каждого нового
 * соединения все зарегистрированные destination подписываются заново, а сообщения
 * продолжают приходить в те же [Flow], что вернули `listen*()`.
 */
internal class DefaultMessagesStompSource(
    httpClient: HttpClient
) : CommonStompSource,
    ClientMessagesStompSource,
    ProMessagesStompSource,
    StompConnectionController {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val stompClient = StompClient(
        webSocketClient = KtorWebSocketClient(httpClient),
        configure = {
            heartBeat = HeartBeat(
                minSendPeriod = 5.seconds,
                expectedPeriod = 5.seconds
            )
        }
    )
    private val json = Json {
        ignoreUnknownKeys = true
    }

    // Защищает connectionJob, subscriptions и подписку на текущую сессию
    private val mutex = Mutex()
    private var connectionJob: Job? = null
    private val subscriptions = mutableMapOf<String, Subscription<*>>()
    private val connectionState = MutableStateFlow<ConnectionState>(ConnectionState.Disconnected(error = null))
    private val reconnectRequests = Channel<Unit>(Channel.CONFLATED)
    private val reconnections = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    private var isFirstAttempt = true

    override suspend fun connect() {
        startCommonSubscriptions()

        val initialState = connectionState.value
        if (initialState is ConnectionState.Connected) return

        mutex.withLock {
            if (connectionJob == null) {
                connectionJob = scope.launch { runConnectionLoop() }
            }
        }
        reconnectRequests.trySend(Unit)

        val result = connectionState.first { state ->
            state is ConnectionState.Connected ||
                state is ConnectionState.Disconnected && state !== initialState && state.error != null
        }
        if (result is ConnectionState.Disconnected) {
            throw checkNotNull(result.error)
        }
    }

    override fun onAppForeground() {
        if (connectionJob != null && connectionState.value !is ConnectionState.Connected) {
            loggerD("App foreground: reconnect now")
            reconnectRequests.trySend(Unit)
        }
    }

    override suspend fun listenReconnections(): Flow<Unit> {
        return reconnections.asSharedFlow()
    }

    override suspend fun register() {
        loggerD("Register: app/executor.register")
        withSession { it.sendEmptyMsg(destination = "/app/executor.register") }
    }

    override suspend fun startChat(message: ClientApiChatRequest) {
        loggerD("Start chat: /app/chat.request. Body: $message")
        withSession {
            it.convertAndSend(
                destination = "/app/chat.request",
                body = message
            )
        }
    }

    override suspend fun endChat(sessionId: String) {
        val message = CommonApiEndChat(sessionId)
        loggerD("End chat: /app/chat.end. Body: $message")
        withSession {
            it.convertAndSend(
                destination = "/app/chat.end",
                body = message
            )
        }
    }

    override suspend fun reconnect(message: CommonApiReconnectMessage) {
        loggerD("Reconnect chat: /app/chat.reconnect. Body: $message")
        withSession {
            it.convertAndSend(
                destination = "/app/chat.reconnect",
                body = message
            )
        }
    }

    override suspend fun acceptChat(message: ProApiAcceptChat) {
        loggerD("Accept chat: /app/chat.accept. Body: $message")
        withSession {
            it.convertAndSend(
                destination = "/app/chat.accept",
                body = message
            )
        }
    }

    override suspend fun send(message: CommonApiSendMessage) {
        loggerD("Send message: /app/chat.send. Body: $message")
        withSession {
            it.convertAndSend(
                destination = "/app/chat.send",
                body = message
            )
        }
    }

    override suspend fun listenSystem(): Flow<CommonApiSystemMessage> {
        return listen(
            destination = "/user/queue/system",
            deserializer = CommonApiSystemMessage.serializer()
        )
    }

    override suspend fun listenErrors(): Flow<CommonApiErrorMessage> {
        return listen(
            destination = "/user/queue/errors",
            deserializer = CommonApiErrorMessage.serializer()
        )
    }

    override suspend fun listenSession(): Flow<CommonApiSessionMessage> {
        return listen(
            destination = "/user/queue/session",
            deserializer = CommonApiSessionMessage.serializer()
        )
    }

    override suspend fun listenIncoming(): Flow<ProApiIncoming> {
        return listen(
            destination = "/user/queue/incoming",
            deserializer = ProApiIncoming.serializer()
        )
    }

    override suspend fun listenMessages(): Flow<CommonApiMessage> {
        return listen(
            destination = "/user/queue/messages",
            deserializer = CommonApiMessage.serializer()
        )
    }

    override suspend fun listenSessionEnd(): Flow<CommonApiMessage> {
        return listen(
            destination = "/user/queue/session-end",
            deserializer = CommonApiMessage.serializer()
        )
    }

    private suspend fun startCommonSubscriptions() {
        listenErrors()
        listenSession()
        listenMessages()
        listenSessionEnd()
    }

    private suspend fun runConnectionLoop() {
        var failedAttempts = 0
        while (true) {
            reconnectRequests.receive()
            connectionState.value = ConnectionState.Connecting

            val error = try {
                val session = openSession()
                failedAttempts = 0
                val cause = session.closed.await()
                loggerE("Connection lost", cause)
                session.close()
                cause
            } catch (e: CancellationException) {
                throw e
            } catch (e: Throwable) {
                loggerE("Connection failed", e)
                failedAttempts++
                e
            }
            connectionState.value = ConnectionState.Disconnected(error ?: ConnectionClosedException())

            // Ждем паузу backoff или явный запрос на переподключение (connect(), возврат в foreground)
            withTimeoutOrNull(reconnectDelay(failedAttempts)) { reconnectRequests.receive() }
            reconnectRequests.trySend(Unit)
        }
    }

    private suspend fun openSession(): ActiveSession {
        // Первое подключение ждет сам вызывающий connect(), остальные - переподключения
        val isReconnection = !isFirstAttempt
        isFirstAttempt = false
        val url = "ws://$BackendHost/ws"
        loggerD("Connect: $url")
        val stompSession = stompClient.connect(url = url).withJsonConversions(json = json)
        val session = ActiveSession(
            session = stompSession,
            scope = CoroutineScope(SupervisorJob(scope.coroutineContext.job) + Dispatchers.Default)
        )

        mutex.withLock {
            try {
                subscriptions.values.forEach { it.subscribe(session) }
            } catch (e: Throwable) {
                session.close()
                throw e
            }
            connectionState.value = ConnectionState.Connected(session)
            loggerD("Connected, subscriptions: ${subscriptions.keys}")
        }

        if (isReconnection) {
            reconnections.tryEmit(Unit)
        }
        return session
    }

    private fun reconnectDelay(failedAttempts: Int): Duration {
        if (failedAttempts == 0) return Duration.ZERO
        val multiplier = 1 shl min(failedAttempts - 1, 5)
        return minOf(InitialReconnectDelay * multiplier, MaxReconnectDelay)
    }

    private suspend fun <T : Any> listen(destination: String, deserializer: KSerializer<T>): Flow<T> {
        return mutex.withLock {
            @Suppress("UNCHECKED_CAST")
            val existing = subscriptions[destination] as Subscription<T>?
            if (existing != null) return@withLock existing.messages

            val subscription = Subscription(destination, deserializer)
            subscriptions[destination] = subscription
            loggerD("Start listening: $destination")

            // Если соединение уже есть - подписываемся сразу, чтобы не пропустить ответы на следующие команды.
            // Иначе подписка произойдет при ближайшем подключении.
            val connected = connectionState.value as? ConnectionState.Connected
            if (connected != null) {
                try {
                    subscription.subscribe(connected.session)
                } catch (e: CancellationException) {
                    throw e
                } catch (e: Throwable) {
                    loggerE("Subscribe $destination failed", e)
                    connected.session.markClosed(e)
                }
            }
            subscription.messages
        }
    }

    private suspend fun <T> withSession(block: suspend (StompSessionWithKxSerialization) -> T): T {
        val session = awaitSession()
        return try {
            block(session.session)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Throwable) {
            session.markClosed(e)
            throw e
        }
    }

    private suspend fun awaitSession(): ActiveSession {
        (connectionState.value as? ConnectionState.Connected)?.let { return it.session }

        reconnectRequests.trySend(Unit)
        val connected = withTimeoutOrNull(SendConnectionTimeout) {
            connectionState.first { it is ConnectionState.Connected } as ConnectionState.Connected
        }
        return connected?.session ?: throw StompNotConnectedException()
    }

    private sealed interface ConnectionState {
        // Обычный класс, а не data: connect() сравнивает состояния по ссылке
        class Disconnected(val error: Throwable?) : ConnectionState
        data object Connecting : ConnectionState
        class Connected(val session: ActiveSession) : ConnectionState
    }

    private class ActiveSession(
        val session: StompSessionWithKxSerialization,
        val scope: CoroutineScope
    ) {
        val closed = CompletableDeferred<Throwable?>()

        fun markClosed(cause: Throwable?) {
            closed.complete(cause)
        }

        suspend fun close() {
            scope.cancel()
            withContext(NonCancellable) {
                runCatching { session.disconnect() }
            }
        }
    }

    private inner class Subscription<T : Any>(
        private val destination: String,
        private val deserializer: KSerializer<T>
    ) {
        private val _messages = MutableSharedFlow<T>(extraBufferCapacity = 64)
        val messages: Flow<T> = _messages.asSharedFlow()

        suspend fun subscribe(session: ActiveSession) {
            val flow = session.session.subscribe(
                destination = destination,
                deserializer = deserializer
            )
            session.scope.launch {
                try {
                    flow.collect {
                        loggerD("Message from $destination: $it")
                        _messages.emit(it)
                    }
                    session.markClosed(null)
                } catch (e: CancellationException) {
                    throw e
                } catch (e: Throwable) {
                    loggerE("Listening $destination error", e)
                    session.markClosed(e)
                }
            }
        }
    }
}

internal class StompNotConnectedException : IllegalStateException("STOMP session is not connected")

internal class ConnectionClosedException : IllegalStateException("STOMP session was closed")
