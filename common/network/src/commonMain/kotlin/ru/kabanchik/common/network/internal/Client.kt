package ru.kabanchik.common.network.internal

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
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
import ru.kabanchik.common.domain.auth.logic.api.repository.CommonAuthTokenRepository
import kotlin.time.Duration.Companion.seconds

internal const val DEFAULT_HOST = "185.102.139.25:8080"
private val DefaultRequestTimeout = 30.seconds

private val AuthPaths = setOf(
    "/auth/api/login-user",
    "/auth/api/register-user",
    "/auth/api/login-executor"
)

internal fun createRestClient(
    tokenRepository: CommonAuthTokenRepository
): HttpClient {
    return HttpClient(CIO) {
        expectSuccess = true

        defaultRequest {
            url("http://$DEFAULT_HOST/")
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
        }
        install(HttpTimeout) {
            requestTimeoutMillis = DefaultRequestTimeout.inWholeMilliseconds
            connectTimeoutMillis = DefaultRequestTimeout.inWholeMilliseconds
            socketTimeoutMillis = DefaultRequestTimeout.inWholeMilliseconds
        }
        install(ContentNegotiation) {
            json(
                Json {
                    prettyPrint = true
                    ignoreUnknownKeys = true
                }
            )
        }
        install(Auth) {
            bearer {
                loadTokens {
                    tokenRepository.getToken()
                        ?.takeIf { it.isNotBlank() }
                        ?.let { BearerTokens(accessToken = it, refreshToken = "") }
                }
                cacheTokens = false
                sendWithoutRequest { request ->
                    request.url.build().encodedPath !in AuthPaths
                }
            }
        }
        install(WebSockets) {
            pingInterval = 20.seconds
            contentConverter = KotlinxWebsocketSerializationConverter(Json)
        }
    }
}
