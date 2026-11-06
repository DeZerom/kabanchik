package ru.kabanchik.pro.domain.chat.logic.internal

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import ru.kabanchik.common.chat.model.CommonSessionStatus
import ru.kabanchik.common.domain.chat.logic.api.CommonChatDetailsInteractor
import ru.kabanchik.pro.domain.chat.logic.api.ProChatDetailsInteractor
import ru.kabanchik.pro.domain.chat.logic.api.repository.ProChatDetailsRepository

internal class DefaultProChatDetailsInteractor(
    commonInteractor: CommonChatDetailsInteractor,
    private val detailsRepository: ProChatDetailsRepository
) : ProChatDetailsInteractor, CommonChatDetailsInteractor by commonInteractor {
    override suspend fun listenPaused(sessionId: String): Flow<Unit> {
        return detailsRepository.listenSession()
            .filter { it.sessionId == sessionId && it.status == CommonSessionStatus.Paused }
            .map { }
    }
}
