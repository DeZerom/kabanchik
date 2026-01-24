package ru.kabanchik.common.network.api

import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.receiveDeserialized
import io.ktor.client.plugins.websocket.sendSerialized
import io.ktor.websocket.close
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface WebSocketSource {
    suspend fun connect(url: String)
    suspend fun doAction(action: suspend DefaultClientWebSocketSession.() -> Unit)
}

suspend fun WebSocketSource.closeSession() {
    doAction { close() }
}

suspend inline fun <reified T> WebSocketSource.sendSerialized(content: T) {
    doAction { sendSerialized(content) }
}

inline fun <reified T> WebSocketSource.listen(): Flow<T> {
    return flow {
        doAction {
            while (true) {
                val message = receiveDeserialized<T>()
                emit(message)
            }
        }
    }
}