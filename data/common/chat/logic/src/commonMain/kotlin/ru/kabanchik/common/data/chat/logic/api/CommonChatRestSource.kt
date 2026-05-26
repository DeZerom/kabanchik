package ru.kabanchik.common.data.chat.logic.api

import ru.kabanchik.common.data.chatDetails.model.CommonApiChatSummary
import ru.kabanchik.common.data.chatDetails.model.CommonApiMessage

interface CommonChatRestSource {
    suspend fun getChats(): List<CommonApiChatSummary>
    suspend fun getMessages(sessionId: String): List<CommonApiMessage>
}
