package ru.kabanchik.common.network.internal

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.pingInterval
import io.ktor.client.request.accept
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import kotlin.time.Duration.Companion.seconds

internal const val DEFAULT_HOST = "185.102.139.25:8080"

internal fun createRestClient(): HttpClient {
    return HttpClient(CIO) {
        expectSuccess = true

        defaultRequest {
            url("http://$DEFAULT_HOST/")
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
        }
        install(ContentNegotiation) {
            json(
                Json {
                    prettyPrint = true
                    ignoreUnknownKeys = true
                }
            )
        }
        install(WebSockets) {
            pingInterval = 20.seconds
            contentConverter = KotlinxWebsocketSerializationConverter(Json)
        }
    }
}
