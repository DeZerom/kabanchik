package ru.kabanchik.common.network.internal

import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.webSocketSession
import ru.kabanchik.common.network.api.WebSocketSource

internal class DefaultWebSocketSource(
    private val client: HttpClient
) : WebSocketSource {
    private var session: DefaultClientWebSocketSession? = null

    override suspend fun connect(url: String) {
        session = client.webSocketSession(urlString = url)
    }

    override suspend fun doAction(action: suspend DefaultClientWebSocketSession.() -> Unit) {
        val notNullSession = session ?: throw IllegalStateException("Call WebSocketSource.doAction() before calling connect()")
        action(notNullSession)
    }
}