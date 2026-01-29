package ru.kabanchik.common.datastore.api

interface DataStoreSource {
    suspend fun setString(key: String, value: String)
    suspend fun getString(key: String): String?
}