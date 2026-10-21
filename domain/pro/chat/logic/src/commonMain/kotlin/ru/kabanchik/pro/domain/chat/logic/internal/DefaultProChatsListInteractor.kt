package ru.kabanchik.pro.domain.chat.logic.internal

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.async
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.selects.select
import ru.kabanchik.common.chat.model.CommonChatError
import ru.kabanchik.common.chat.model.CommonChatErrorCode
import ru.kabanchik.common.chat.model.CommonSessionMessage
import ru.kabanchik.common.chat.model.CommonSessionStatus
import ru.kabanchik.common.domain.chat.logic.api.CommonChatsListInteractor
import ru.kabanchik.pro.domain.chat.logic.api.ExecutorOffShiftException
import ru.kabanchik.pro.domain.chat.logic.api.ProChatsListInteractor
import ru.kabanchik.pro.domain.chat.logic.api.repository.ProChatsListRepository
import ru.kabanchik.pro.domain.chatDetails.model.ProIncoming
import ru.kabanchik.pro.domain.shift.logic.api.ProShiftInteractor
import ru.kabanchik.pro.domain.shift.model.ProShiftStatus

internal class DefaultProChatsListInteractor(
    commonInteractor: CommonChatsListInteractor,
    private val listRepository: ProChatsListRepository,
    private val shiftInteractor: ProShiftInteractor
) : ProChatsListInteractor, CommonChatsListInteractor by commonInteractor {
    override suspend fun requestChat(): CommonSessionMessage {
        val errors = listRepository.listenErrors()
        val sessions = listRepository.listenSession()
        val incomingFlow = listRepository.listenIncoming()
        val reconnections = listRepository.listenReconnections()

        return coroutineScope {
            // Подписки запускаются UNDISPATCHED, чтобы не пропустить ответы на команды ниже
            launch(start = CoroutineStart.UNDISPATCHED) {
                errors.first { it.code == CommonChatErrorCode.ExecutorOffShift }
                shiftInteractor.markOffShift()
                throw ExecutorOffShiftException()
            }
            val incoming = Channel<ProIncoming>(Channel.UNLIMITED)
            launch(start = CoroutineStart.UNDISPATCHED) {
                incomingFlow.collect { incoming.send(it) }
            }

            listRepository.register()

            // Регистрация привязана к STOMP-сессии: после переподключения ее нужно повторить, но только на смене
            launch {
                reconnections.collect { reRegister() }
            }

            val rejectedSessionIds = mutableSetOf<String>()
            var session: CommonSessionMessage? = null
            while (session == null) {
                val candidate = incoming.receive()
                if (candidate.sessionId in rejectedSessionIds) continue

                session = acceptChat(candidate.sessionId, sessions, errors)
                if (session == null) {
                    rejectedSessionIds += candidate.sessionId
                }
            }
            coroutineContext.cancelChildren()
            session
        }
    }

    override suspend fun listenPausedChats(): Flow<String> {
        return listRepository.listenSession()
            .filter { it.status == CommonSessionStatus.Paused }
            .map { it.sessionId }
    }

    private suspend fun reRegister() {
        val status = try {
            shiftInteractor.refresh()
        } catch (e: CancellationException) {
            throw e
        } catch (_: Throwable) {
            // Состояние смены неизвестно: регистрируемся, вне смены сервер ответит EXECUTOR_OFF_SHIFT
            ProShiftStatus.OnShift
        }
        if (status == ProShiftStatus.OffShift) throw ExecutorOffShiftException()

        listRepository.register()
    }

    /**
     * Принимает заявку. Возвращает `null`, если ее уже принял другой оператор или чат закрыт.
     */
    private suspend fun CoroutineScope.acceptChat(
        sessionId: String,
        sessions: Flow<CommonSessionMessage>,
        errors: Flow<CommonChatError>
    ): CommonSessionMessage? {
        val accepted = async(start = CoroutineStart.UNDISPATCHED) {
            sessions.first { it.sessionId == sessionId && it.status == CommonSessionStatus.Open }
        }
        val unavailable = async(start = CoroutineStart.UNDISPATCHED) {
            errors.first { it.code == CommonChatErrorCode.SessionNotAvailable && it.sessionId == sessionId }
        }

        listRepository.acceptChat(sessionId = sessionId)

        val session = select {
            accepted.onAwait { it }
            unavailable.onAwait { null }
        }
        accepted.cancel()
        unavailable.cancel()
        return session
    }
}
