package ru.kabanchik.common.network.internal.api.chat

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import ru.kabanchik.pro.data.chat.logic.api.ProShiftRestSource
import ru.kabanchik.pro.data.chatDetails.model.ProApiShift

internal class DefaultProShiftRestSource(
    private val httpClient: HttpClient
) : ProShiftRestSource {
    override suspend fun getShift(): ProApiShift {
        return httpClient.get(urlString = "/chat/api/executor/shift").body()
    }

    override suspend fun startShift(): ProApiShift {
        return httpClient.post(urlString = "/chat/api/executor/shift/start").body()
    }

    override suspend fun endShift(): ProApiShift {
        return httpClient.post(urlString = "/chat/api/executor/shift/end").body()
    }
}
