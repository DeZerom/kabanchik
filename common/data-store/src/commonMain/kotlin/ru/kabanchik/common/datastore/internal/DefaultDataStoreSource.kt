package ru.kabanchik.common.datastore.internal

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import ru.kabanchik.common.datastore.api.DataStoreSource

class DefaultDataStoreSource(
    private val dataStore: DataStore<Preferences>
) : DataStoreSource {
    override suspend fun setString(key: String, value: String) {
        setValue(stringPreferencesKey(key), value)
    }

    override suspend fun getString(key: String): String? {
        return getValue(stringPreferencesKey(key))
    }

    private suspend fun <T> setValue(key: Preferences.Key<T>, value: T) {
        dataStore.edit {
            it[key] = value
        }
    }

    private suspend fun <T: Any> getValue(key: Preferences.Key<T>): T? {
        return dataStore.data.first()[key]
    }
}