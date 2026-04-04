package ru.kabanchik.client

import org.koin.core.context.startKoin
import ru.kabanchik.pro.di.proAppModules

fun initKoin() {
    startKoin {
        modules(proAppModules())
    }
}