package ru.kabanchik.client.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import okio.Path.Companion.toOkioPath
import ru.kabanchik.common.datastore.api.PreferencesDataStoreProvider
import java.io.File

class JvmPreferencesDataStoreProvider : PreferencesDataStoreProvider {
    override fun get(): DataStore<Preferences> {
        return PreferenceDataStoreFactory.createWithPath(
            produceFile = {
                val os = System.getProperty("os.name").lowercase()
                val userHome = System.getProperty("user.home")
                val appName = "Kabanchik"

                val directory = when {
                    os.contains("win") ->
                        File(System.getenv("AppData"), appName)
                    os.contains("mac") ->
                        File(userHome, "Library/Application Support/$appName")
                    else -> // Linux/Unix
                        File(userHome, ".local/share/$appName")
                }

                if (!directory.exists()) directory.mkdirs()

                File(directory, PreferencesDataStoreProvider.FileName).toOkioPath()
            }
        )
    }
}