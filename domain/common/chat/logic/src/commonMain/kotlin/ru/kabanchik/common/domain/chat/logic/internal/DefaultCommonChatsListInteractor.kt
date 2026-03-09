package ru.kabanchik.common.domain.chat.logic.internal

import ru.kabanchik.common.domain.chat.logic.api.CommonChatsListInteractor
import ru.kabanchik.common.domain.chat.logic.api.repository.CommonChatTokenRepository
import ru.kabanchik.common.domain.chat.logic.api.repository.CommonChatsListRepository

internal class DefaultCommonChatsListInteractor(
    private val tokenRepository: CommonChatTokenRepository,
    private val listRepository: CommonChatsListRepository
) : CommonChatsListInteractor {
    override suspend fun connect() {
        val token = tokenRepository.getToken().orEmpty()
        listRepository.connect(token)
    }
}