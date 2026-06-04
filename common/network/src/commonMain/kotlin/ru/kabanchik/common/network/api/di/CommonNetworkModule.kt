package ru.kabanchik.common.network.api.di

import io.ktor.client.HttpClient
import org.koin.dsl.bind
import org.koin.dsl.binds
import org.koin.dsl.module
import ru.kabanchik.client.data.auth.logic.api.sources.ClientAuthApi
import ru.kabanchik.client.data.chat.logic.api.ClientMessagesStompSource
import ru.kabanchik.common.data.chat.logic.api.CommonChatRestSource
import ru.kabanchik.common.data.chat.logic.api.CommonStompSource
import ru.kabanchik.common.network.internal.api.auth.DefaultClientAuthApi
import ru.kabanchik.common.network.internal.api.auth.DefaultProAuthApi
import ru.kabanchik.common.network.internal.api.chat.DefaultCommonChatRestSource
import ru.kabanchik.common.network.internal.createRestClient
import ru.kabanchik.common.network.internal.ws.DefaultMessagesStompSource
import ru.kabanchik.pro.data.auth.logic.api.sources.ProAuthApi
import ru.kabanchik.pro.data.chat.logic.api.ProMessagesStompSource

object CommonNetworkModule {
    val module = module {
        single<HttpClient> {
            createRestClient()
        }

        single {
            DefaultMessagesStompSource(
                httpClient = get()
            )
        } binds arrayOf(
            CommonStompSource::class,
            ClientMessagesStompSource::class,
            ProMessagesStompSource::class
        )

        factory {
            DefaultClientAuthApi(
                httpClient = get()
            )
        } bind ClientAuthApi::class
        factory {
            DefaultProAuthApi(
                client = get()
            )
        } bind ProAuthApi::class
        factory {
            DefaultCommonChatRestSource(
                httpClient = get()
            )
        } bind CommonChatRestSource::class
    }
}
