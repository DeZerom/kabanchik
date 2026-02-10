package ru.kabanchik.common.network.internal.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import ru.kabanchik.pro.data.auth.logic.api.sources.ProAuthApi
import ru.kabanchik.pro.data.auth.model.ApiProAuthResult
import ru.kabanchik.pro.data.auth.model.ApiProCredentials

class DefaultProAuthApi(
    private val client: HttpClient
) : ProAuthApi {
    override suspend fun authorize(credentials: ApiProCredentials): ApiProAuthResult {
        return client.post(urlString = "/api/login") {
            setBody(credentials)
        }.body()
    }
}