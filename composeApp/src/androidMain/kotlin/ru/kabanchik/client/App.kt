package ru.kabanchik.client

import android.app.Application
import org.koin.core.context.startKoin
import ru.kabanchik.client.di.appModules

class App: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            modules(appModules())
        }
    }
}