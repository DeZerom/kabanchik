package ru.kabanchik.common.datastore.api.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.kabanchik.common.datastore.api.DataStoreSource
import ru.kabanchik.common.datastore.api.PreferencesDataStoreProvider
import ru.kabanchik.common.datastore.internal.DefaultDataStoreSource

object CommonDataStoreModule {
    val module = module {
        singleOf(PreferencesDataStoreProvider::get)
        singleOf(::DefaultDataStoreSource) bind DataStoreSource::class
    }
}