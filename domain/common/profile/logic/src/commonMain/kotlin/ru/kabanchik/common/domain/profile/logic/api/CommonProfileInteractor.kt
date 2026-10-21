package ru.kabanchik.common.domain.profile.logic.api

interface CommonProfileInteractor {
    suspend fun getUserLogin(): String?

    /**
     * Разрывает соединение с сервером и удаляет все локальные данные пользователя.
     */
    suspend fun logout()
}
