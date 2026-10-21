package ru.kabanchik.common.data.user.logic.internal

import ru.kabanchik.common.data.chat.logic.api.CommonStompSource
import ru.kabanchik.common.datastore.api.DataStoreSource
import ru.kabanchik.common.domain.auth.logic.api.repository.CommonAuthUserRepository
import ru.kabanchik.common.domain.profile.logic.api.repository.CommonProfileUserRepository
import ru.kabanchik.common.domain.user.logic.api.repository.UserRepository

private const val UserLoginKey = "user_login"

class DefaultUserRepository(
    private val dataStoreSource: DataStoreSource,
    private val stompSource: CommonStompSource
) : UserRepository, CommonAuthUserRepository, CommonProfileUserRepository {
    override suspend fun setUserLogin(login: String) {
        dataStoreSource.setString(UserLoginKey, login)
    }

    override suspend fun getUserLogin(): String? {
        return dataStoreSource.getString(UserLoginKey)
    }

    override suspend fun disconnect() {
        stompSource.disconnect()
    }

    override suspend fun clearLocalData() {
        dataStoreSource.clear()
    }
}