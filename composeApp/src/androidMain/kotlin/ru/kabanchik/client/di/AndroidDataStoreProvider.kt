package ru.kabanchik.client.di

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import okio.Path.Companion.toPath
import ru.kabanchik.common.datastore.api.PreferencesDataStoreProvider

class AndroidDataStoreProvider(
    private val application: Application
) : PreferencesDataStoreProvider {
    override fun get(): DataStore<Preferences> {
        return PreferenceDataStoreFactory.createWithPath(
            produceFile = { application.filesDir.resolve(PreferencesDataStoreProvider.FileName).absolutePath.toPath() }
        )
    }
}