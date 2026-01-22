package ru.kabanchik.client

import org.koin.core.context.startKoin
import ru.kabanchik.client.di.appModules

fun initKoin() {
    startKoin {
        modules(appModules())
    }
}