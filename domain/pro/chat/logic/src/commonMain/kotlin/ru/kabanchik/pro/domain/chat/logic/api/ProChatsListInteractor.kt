package ru.kabanchik.pro.domain.chat.logic.api

interface ProChatsListInteractor {
    suspend fun connect()
    suspend fun requestChat()
}