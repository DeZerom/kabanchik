package ru.kabanchik.client

import org.koin.core.context.startKoin
import ru.kabanchik.client.di.clientAppModules

fun initKoin() {
    startKoin {
        modules(clientAppModules())
    }
}