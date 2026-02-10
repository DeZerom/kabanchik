package ru.kabanchik.common.network.api.di

import io.ktor.client.HttpClient
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.kabanchik.client.data.auth.logic.api.sources.AuthApi
import ru.kabanchik.client.data.chatDetails.logic.api.MessagesStompSource
import ru.kabanchik.common.network.internal.DefaultMessagesStompSource
import ru.kabanchik.common.network.internal.api.DefaultAuthApi
import ru.kabanchik.common.network.internal.api.DefaultProAuthApi
import ru.kabanchik.common.network.internal.createClient
import ru.kabanchik.pro.data.auth.logic.api.sources.ProAuthApi

object CommonNetworkModule {
    val module = module {
        singleOf<HttpClient>(::createClient)
        factoryOf(::DefaultMessagesStompSource) bind MessagesStompSource::class

        factoryOf(::DefaultAuthApi) bind AuthApi::class
        factoryOf(::DefaultProAuthApi) bind ProAuthApi::class
    }
}