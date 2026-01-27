package ru.kabanchik.common.datastore.api

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

interface PreferencesDataStoreProvider {
    fun get(): DataStore<Preferences>

    companion object {
        const val FileName = "datastore.preferences_db"
    }
}