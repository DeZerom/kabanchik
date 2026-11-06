package ru.kabanchik.pro.data.chat.logic.internal

import ru.kabanchik.common.domain.chat.logic.api.repository.CommonChatDetailsRepository
import ru.kabanchik.pro.data.chat.logic.api.ProMessagesStompSource
import ru.kabanchik.pro.domain.chat.logic.api.repository.ProChatDetailsRepository

internal class DefaultProChatDetailsRepository(
    commonRepository: CommonChatDetailsRepository,
    private val stompSource: ProMessagesStompSource
) : ProChatDetailsRepository, CommonChatDetailsRepository by commonRepository {
    override suspend fun endChat(sessionId: String) {
        stompSource.endChat(sessionId)
    }
}
