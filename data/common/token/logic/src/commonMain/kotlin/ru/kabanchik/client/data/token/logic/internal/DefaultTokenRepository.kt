package ru.kabanchik.client.data.token.logic.internal

import ru.kabanchik.client.domain.token.logic.api.TokenRepository
import ru.kabanchik.common.datastore.api.DataStoreSource
import ru.kabanchik.common.domain.auth.logic.api.repository.CommonAuthTokenRepository
import ru.kabanchik.common.domain.chat.logic.api.repository.CommonChatTokenRepository

private const val TokenKey = "auth_token"

class DefaultTokenRepository(
    private val dataStoreSource: DataStoreSource
) : TokenRepository, CommonAuthTokenRepository, CommonChatTokenRepository {
    override suspend fun saveToken(token: String) {
        dataStoreSource.setString(TokenKey, token)
    }

    override suspend fun getToken(): String? {
        return dataStoreSource.getString(TokenKey)
    }
}