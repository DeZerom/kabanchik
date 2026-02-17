package ru.kabanchik.client.data.token.logic.internal

import ru.kabanchik.client.domain.auth.logic.api.repository.AuthTokenRepository
import ru.kabanchik.client.domain.token.logic.api.TokenRepository
import ru.kabanchik.common.datastore.api.DataStoreSource
import ru.kabanchik.pro.domain.auth.logic.api.repository.ProAuthTokenRepository
import ru.kabanchik.pro.domain.chatDetails.logic.api.repository.ProChatDetailsTokenRepository

private const val TokenKey = "auth_token"

class DefaultTokenRepository(
    private val dataStoreSource: DataStoreSource
) : TokenRepository, AuthTokenRepository, ProAuthTokenRepository, ProChatDetailsTokenRepository {
    override suspend fun saveToken(token: String) {
        dataStoreSource.setString(TokenKey, token)
    }

    override suspend fun getToken(): String? {
        return dataStoreSource.getString(TokenKey)
    }
}