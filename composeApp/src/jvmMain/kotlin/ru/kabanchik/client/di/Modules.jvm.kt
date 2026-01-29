package ru.kabanchik.client.di

import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.kabanchik.common.datastore.api.PreferencesDataStoreProvider

private val jvmModule = module {
    singleOf(::JvmPreferencesDataStoreProvider) bind PreferencesDataStoreProvider::class
}

actual val platformModules: List<Module> = listOf(
    jvmModule
)