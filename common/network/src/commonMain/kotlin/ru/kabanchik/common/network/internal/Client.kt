package ru.kabanchik.common.network.internal

import dev.shivathapaa.logger.api.loggerD
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.BinaryLogBodyFilter
import io.ktor.client.plugins.logging.BodyFilterResult
import io.ktor.client.plugins.logging.LogBodyFilter
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.pingInterval
import io.ktor.client.request.accept
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.Url
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import io.ktor.serialization.kotlinx.json.json
import io.ktor.utils.io.ByteReadChannel
import kotlinx.serialization.json.Json
import ru.kabanchik.common.domain.auth.logic.api.repository.CommonAuthTokenRepository
import ru.kabanchik.common.tools.network.BackendBaseUrl
import kotlin.time.Duration.Companion.seconds

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
            url("$BackendBaseUrl/")
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
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    loggerD("REST: $message")
                }
            }
            level = LogLevel.BODY
            bodyFilter = ResponseBodyLogFilter
            filter { request ->
                request.url.build().encodedPath !in AuthPaths
            }
            sanitizeHeader { header ->
                header.equals(HttpHeaders.Authorization, ignoreCase = true)
            }
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

private object ResponseBodyLogFilter : LogBodyFilter {
    override suspend fun filterRequest(
        url: Url,
        contentLength: Long?,
        contentType: ContentType?,
        headers: Headers,
        body: ByteReadChannel,
    ): BodyFilterResult {
        return BodyFilterResult.Skip(
            reason = "request body logging disabled",
            byteSize = contentLength,
        )
    }

    override suspend fun filterResponse(
        url: Url,
        contentLength: Long?,
        contentType: ContentType?,
        headers: Headers,
        body: ByteReadChannel,
    ): BodyFilterResult {
        return BinaryLogBodyFilter.filterResponse(
            url = url,
            contentLength = contentLength,
            contentType = contentType,
            headers = headers,
            body = body,
        )
    }
}
