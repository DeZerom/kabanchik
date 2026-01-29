package ru.kabanchik.client.data.token.logic.internal

import ru.kabanchik.client.domain.auth.logic.api.repository.AuthTokenRepository
import ru.kabanchik.client.domain.token.logic.api.TokenRepository
import ru.kabanchik.common.datastore.api.DataStoreSource

private const val TokenKey = "auth_token"

class DefaultTokenRepository(
    private val dataStoreSource: DataStoreSource
) : TokenRepository, AuthTokenRepository {
    override suspend fun saveToken(token: String) {
        dataStoreSource.setString(TokenKey, token)
    }

    override suspend fun getToken(): String? {
        return dataStoreSource.getString(TokenKey)
    }
}