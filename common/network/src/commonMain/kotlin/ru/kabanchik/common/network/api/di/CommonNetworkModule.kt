package ru.kabanchik.common.network.api.di

import io.ktor.client.HttpClient
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.kabanchik.common.network.api.WebSocketSource
import ru.kabanchik.common.network.internal.DefaultWebSocketSource
import ru.kabanchik.common.network.internal.createClient

object CommonNetworkModule {
    val module = module {
        singleOf<HttpClient>(::createClient)
        factoryOf(::DefaultWebSocketSource) bind WebSocketSource::class
    }
}