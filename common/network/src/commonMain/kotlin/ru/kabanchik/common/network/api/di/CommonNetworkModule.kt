package ru.kabanchik.common.network.api.di

import io.ktor.client.HttpClient
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.binds
import org.koin.dsl.module
import ru.kabanchik.client.data.auth.logic.api.sources.AuthApi
import ru.kabanchik.client.data.chat.logic.api.ClientMessagesStompSource
import ru.kabanchik.common.data.chat.logic.api.CommonStompSource
import ru.kabanchik.common.network.internal.api.DefaultAuthApi
import ru.kabanchik.common.network.internal.api.DefaultProAuthApi
import ru.kabanchik.common.network.internal.createClient
import ru.kabanchik.common.network.internal.ws.DefaultMessagesStompSource
import ru.kabanchik.pro.data.auth.logic.api.sources.ProAuthApi
import ru.kabanchik.pro.data.chat.logic.api.ProMessagesStompSource

object CommonNetworkModule {
    val module = module {
        singleOf<HttpClient>(::createClient)
        singleOf(::DefaultMessagesStompSource) binds arrayOf(
            CommonStompSource::class,
            ClientMessagesStompSource::class,
            ProMessagesStompSource::class
        )

        factoryOf(::DefaultAuthApi) bind AuthApi::class
        factoryOf(::DefaultProAuthApi) bind ProAuthApi::class
    }
}