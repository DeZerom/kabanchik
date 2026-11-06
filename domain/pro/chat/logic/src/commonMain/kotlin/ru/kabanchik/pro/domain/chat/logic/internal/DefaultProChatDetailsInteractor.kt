package ru.kabanchik.pro.domain.chat.logic.internal

import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.selects.select
import kotlinx.coroutines.withTimeoutOrNull
import ru.kabanchik.common.chat.model.CommonChatError
import ru.kabanchik.common.chat.model.CommonChatErrorCode
import ru.kabanchik.common.chat.model.CommonSessionStatus
import ru.kabanchik.common.domain.chat.logic.api.CommonChatDetailsInteractor
import ru.kabanchik.pro.domain.chat.logic.api.ExecutorOffShiftException
import ru.kabanchik.pro.domain.chat.logic.api.ProChatDetailsInteractor
import ru.kabanchik.pro.domain.chat.logic.api.ProEndChatException
import ru.kabanchik.pro.domain.chat.logic.api.repository.ProChatDetailsRepository
import ru.kabanchik.pro.domain.shift.logic.api.ProShiftInteractor
import kotlin.time.Duration.Companion.seconds

private val EndChatTimeout = 10.seconds

private val EndChatErrorCodes = setOf(
    CommonChatErrorCode.ExecutorOffShift,
    CommonChatErrorCode.Forbidden,
    CommonChatErrorCode.InvalidEndRequest,
    CommonChatErrorCode.NotSessionExecutor,
    CommonChatErrorCode.SessionNotOpen,
    CommonChatErrorCode.SessionEndFailed,
)

internal class DefaultProChatDetailsInteractor(
    private val commonInteractor: CommonChatDetailsInteractor,
    private val detailsRepository: ProChatDetailsRepository,
    private val shiftInteractor: ProShiftInteractor
) : ProChatDetailsInteractor, CommonChatDetailsInteractor by commonInteractor {
    override suspend fun listenPaused(sessionId: String): Flow<Unit> {
        return detailsRepository.listenSession()
            .filter { it.sessionId == sessionId && it.status == CommonSessionStatus.Paused }
            .map { }
    }

    override suspend fun endChat(sessionId: String) {
        when (val result = sendEndChat(sessionId)) {
            EndChatResult.Closed -> Unit
            is EndChatResult.Rejected -> handleRejection(sessionId, result.error)
            // Событие могло потеряться: фактическое состояние берем из REST
            EndChatResult.NoResponse -> ensureClosed(sessionId, code = null)
        }
    }

    private suspend fun sendEndChat(sessionId: String): EndChatResult {
        val sessions = detailsRepository.listenSession()
        val sessionEnds = detailsRepository.listenSessionEnd()
        val errors = detailsRepository.listenErrors()

        return coroutineScope {
            // Подписки запускаются UNDISPATCHED, чтобы не пропустить ответ на команду ниже
            val closed = async(start = CoroutineStart.UNDISPATCHED) {
                sessions.first { it.sessionId == sessionId && it.status == CommonSessionStatus.Closed }
            }
            val ended = async(start = CoroutineStart.UNDISPATCHED) {
                sessionEnds.first { it.sessionId == sessionId }
            }
            val rejected = async(start = CoroutineStart.UNDISPATCHED) {
                errors.first { it.sessionId == sessionId && it.code in EndChatErrorCodes }
            }

            detailsRepository.endChat(sessionId)

            val result = withTimeoutOrNull(EndChatTimeout) {
                select {
                    closed.onAwait { EndChatResult.Closed }
                    ended.onAwait { EndChatResult.Closed }
                    rejected.onAwait { EndChatResult.Rejected(it) }
                }
            }
            closed.cancel()
            ended.cancel()
            rejected.cancel()
            result ?: EndChatResult.NoResponse
        }
    }

    private suspend fun handleRejection(sessionId: String, error: CommonChatError) {
        when (error.code) {
            CommonChatErrorCode.ExecutorOffShift -> {
                shiftInteractor.markOffShift()
                throw ExecutorOffShiftException()
            }
            // Заявка могла закрыться, несмотря на ошибку
            CommonChatErrorCode.SessionEndFailed -> ensureClosed(sessionId, error.code)
            else -> throw ProEndChatException(error.code)
        }
    }

    private suspend fun ensureClosed(sessionId: String, code: CommonChatErrorCode?) {
        if (detailsRepository.getChatStatus(sessionId) != CommonSessionStatus.Closed) {
            throw ProEndChatException(code)
        }
    }

    private sealed interface EndChatResult {
        data object Closed : EndChatResult
        class Rejected(val error: CommonChatError) : EndChatResult
        data object NoResponse : EndChatResult
    }
}
