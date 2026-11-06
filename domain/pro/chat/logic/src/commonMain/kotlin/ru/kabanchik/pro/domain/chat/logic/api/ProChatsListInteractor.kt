package ru.kabanchik.pro.domain.chat.logic.api

import kotlinx.coroutines.flow.Flow
import ru.kabanchik.common.chat.model.CommonChatSummary
import ru.kabanchik.common.chat.model.CommonMessage
import ru.kabanchik.common.chat.model.CommonSessionMessage

interface ProChatsListInteractor {
    suspend fun connect()
    suspend fun listenReconnections(): Flow<Unit>
    suspend fun getChats(): List<CommonChatSummary>
    suspend fun listenMessages(): Flow<CommonMessage>

    /**
     * Ждет заявку и принимает ее. Заявки, которые успел принять другой оператор, пропускает.
     * Бросает [ExecutorOffShiftException], если оператор вне смены.
     */
    suspend fun requestChat(): CommonSessionMessage

    /**
     * `sessionId` чатов, которые поставили на паузу и сняли с оператора.
     */
    suspend fun listenPausedChats(): Flow<String>
}
