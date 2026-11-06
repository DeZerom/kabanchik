package ru.kabanchik.pro.feature.chat.internal.end

import kabanchik.features.pro.chat.generated.resources.Res
import kabanchik.features.pro.chat.generated.resources.chat_end_error
import kabanchik.features.pro.chat.generated.resources.chat_end_off_shift_error
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import ru.kabanchik.common.errorHandler.logic.api.ErrorHandler
import ru.kabanchik.common.store.BaseCoroutineStore
import ru.kabanchik.common.tools.extensions.asTextResource
import ru.kabanchik.pro.domain.chat.logic.api.ExecutorOffShiftException
import ru.kabanchik.pro.domain.chat.logic.api.ProChatDetailsInteractor
import ru.kabanchik.pro.domain.chat.logic.api.ProEndChatException
import ru.kabanchik.pro.feature.chat.api.end.ProEndChatContract.Event
import ru.kabanchik.pro.feature.chat.api.end.ProEndChatContract.SideEffect
import ru.kabanchik.pro.feature.chat.api.end.ProEndChatContract.State

internal class ProEndChatStore(
    private val chatDetailsInteractor: ProChatDetailsInteractor,
    private val errorHandler: ErrorHandler,
    private val sessionId: String,
) : BaseCoroutineStore<Event, State, SideEffect>() {
    override fun initState(): State {
        return State()
    }

    override fun handleEvent(event: Event) {
        when (event) {
            is Event.ResolutionSelected -> selectResolution(event)
            Event.EndClicked -> endChat()
        }
    }

    private fun selectResolution(event: Event.ResolutionSelected) {
        if (currentState.isEnding) return
        reduceState { copy(selectedResolution = event.resolution) }
    }

    private fun endChat() {
        if (currentState.isEnding) return

        reduceState { copy(isEnding = true) }
        coroutineScope.launch {
            try {
                // Выбранный итог не передается: сервер пока принимает только sessionId
                chatDetailsInteractor.endChat(sessionId)
                pushSideEffect(SideEffect.Finished)
            } catch (e: CancellationException) {
                throw e
            } catch (_: ExecutorOffShiftException) {
                pushSideEffect(SideEffect.Error(Res.string.chat_end_off_shift_error.asTextResource()))
            } catch (_: ProEndChatException) {
                pushSideEffect(SideEffect.Error(Res.string.chat_end_error.asTextResource()))
            } catch (e: Throwable) {
                pushSideEffect(SideEffect.Error(errorHandler.handleError(e).defaultMessage))
            } finally {
                reduceState { copy(isEnding = false) }
            }
        }
    }
}
