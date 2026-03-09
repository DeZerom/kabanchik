package ru.kabanchik.common.data.chat.logic.api

interface CommonStompSource {
    suspend fun connect(token: String)
}