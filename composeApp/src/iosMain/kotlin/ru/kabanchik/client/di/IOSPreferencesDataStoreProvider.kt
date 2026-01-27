package ru.kabanchik.client.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import kotlinx.cinterop.ExperimentalForeignApi
import okio.Path.Companion.toPath
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask
import ru.kabanchik.common.datastore.api.PreferencesDataStoreProvider

class IOSPreferencesDataStoreProvider : PreferencesDataStoreProvider {
    @OptIn(ExperimentalForeignApi::class)
    override fun get(): DataStore<Preferences> {
        return PreferenceDataStoreFactory.createWithPath(
            produceFile = {
                val documentDirectory: NSURL? = NSFileManager.defaultManager.URLForDirectory(
                    directory = NSDocumentDirectory,
                    inDomain = NSUserDomainMask,
                    appropriateForURL = null,
                    create = false,
                    error = null,
                )

                (requireNotNull(documentDirectory).path + "/${PreferencesDataStoreProvider.FileName}").toPath()
            }
        )
    }
}