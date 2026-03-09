package ru.kabanchik.common.domain.chat.logic.api.repository

interface CommonChatsListRepository {
    suspend fun connect(token: String)
}