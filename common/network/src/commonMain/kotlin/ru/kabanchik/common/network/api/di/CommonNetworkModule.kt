package ru.kabanchik.common.network.api.di

import io.ktor.client.HttpClient
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.binds
import org.koin.dsl.module
import ru.kabanchik.client.data.auth.logic.api.sources.ClientAuthApi
import ru.kabanchik.client.data.chat.logic.api.ClientMessagesStompSource
import ru.kabanchik.common.data.chat.logic.api.CommonChatRestSource
import ru.kabanchik.common.data.chat.logic.api.CommonStompSource
import ru.kabanchik.common.network.api.NetworkClientQualifier
import ru.kabanchik.common.network.internal.api.auth.DefaultClientAuthApi
import ru.kabanchik.common.network.internal.api.auth.DefaultProAuthApi
import ru.kabanchik.common.network.internal.api.chat.DefaultCommonChatRestSource
import ru.kabanchik.common.network.internal.createRestClient
import ru.kabanchik.common.network.internal.ws.DefaultMessagesStompSource
import ru.kabanchik.pro.data.auth.logic.api.sources.ProAuthApi
import ru.kabanchik.pro.data.chat.logic.api.ProMessagesStompSource

object CommonNetworkModule {
    val module = module {
        singleRestHttpClient(
            qualifier = NetworkClientQualifier.Base,
            port = 8081
        )
        singleRestHttpClient(
            qualifier = NetworkClientQualifier.Chat,
            port = 8080
        )

        single {
            DefaultMessagesStompSource(
                httpClient = get(named(NetworkClientQualifier.Base))
            )
        } binds arrayOf(
            CommonStompSource::class,
            ClientMessagesStompSource::class,
            ProMessagesStompSource::class
        )

        factory {
            DefaultClientAuthApi(
                httpClient = get(named(NetworkClientQualifier.Base))
            )
        } bind ClientAuthApi::class
        factory {
            DefaultProAuthApi(
                client = get(named(NetworkClientQualifier.Base))
            )
        } bind ProAuthApi::class
        factory {
            DefaultCommonChatRestSource(
                httpClient = get(named(NetworkClientQualifier.Chat))
            )
        } bind CommonChatRestSource::class
    }

    private fun Module.singleRestHttpClient(
        qualifier: String,
        port: Int,
        host: String = "185.102.139.25"
    ) {
        single<HttpClient>(named(qualifier)) {
            createRestClient(
                host = host,
                port = port
            )
        }
    }
}
