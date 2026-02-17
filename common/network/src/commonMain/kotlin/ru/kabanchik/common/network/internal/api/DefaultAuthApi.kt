package ru.kabanchik.common.network.internal.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import ru.kabanchik.client.data.auht.model.ApiAuthResult
import ru.kabanchik.client.data.auht.model.ApiCredentials
import ru.kabanchik.client.data.auth.logic.api.sources.AuthApi

internal class DefaultAuthApi(
    private val httpClient: HttpClient
) : AuthApi {
    override suspend fun authorize(credentials: ApiCredentials): ApiAuthResult {
        return httpClient.post(urlString = "/api/login-user") {
            setBody(credentials)
        }.body()
    }

    override suspend fun register(credentials: ApiCredentials): ApiAuthResult {
        return httpClient.post(urlString = "/api/register-user") {
            setBody(credentials)
        }.body()
    }
}