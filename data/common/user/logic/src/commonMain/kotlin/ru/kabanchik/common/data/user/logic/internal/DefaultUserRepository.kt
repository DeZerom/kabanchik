package ru.kabanchik.common.data.user.logic.internal

import ru.kabanchik.common.datastore.api.DataStoreSource
import ru.kabanchik.common.domain.user.logic.api.repository.UserRepository

private const val UserLoginKey = "user_login"

class DefaultUserRepository(
    private val dataStoreSource: DataStoreSource
) : UserRepository {
    override suspend fun setUserLogin(login: String) {
        dataStoreSource.setString(UserLoginKey, login)
    }

    override suspend fun getUserLogin(): String? {
        return dataStoreSource.getString(UserLoginKey)
    }
}