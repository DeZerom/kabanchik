package ru.kabanchik.pro.feature.chat.internal.details

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import ru.kabanchik.common.chat.model.CommonChatMessage
import ru.kabanchik.common.domain.user.logic.api.UserInteractor
import ru.kabanchik.common.errorHandler.logic.api.ErrorHandler
import ru.kabanchik.common.features.chat.logic.details.toState
import ru.kabanchik.common.store.BaseCoroutineStore
import ru.kabanchik.pro.domain.chat.logic.api.ProChatDetailsInteractor
import ru.kabanchik.pro.feature.chat.api.details.ProChatDetailsContract.Event
import ru.kabanchik.pro.feature.chat.api.details.ProChatDetailsContract.SideEffect
import ru.kabanchik.pro.feature.chat.api.details.ProChatDetailsContract.State

internal class ProChatDetailsStore(
    private val chatDetailsInteractor: ProChatDetailsInteractor,
    private val userInteractor: UserInteractor,
    private val errorHandler: ErrorHandler
) : BaseCoroutineStore<Event, State, SideEffect>() {
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        pushSideEffect(SideEffect.Error(errorHandler.handleError(throwable).defaultMessage))
    }

    override fun initState(): State {
        return State()
    }

    override fun handleEvent(event: Event) {
        when (event) {
            Event.MessageSent -> sendMessage()
            is Event.MessageTextChanged -> reduceState { copy(currentMessage = event.newText) }
        }
    }

    init {
        initChat()
    }

    private fun initChat() {
        coroutineScope.launch(coroutineExceptionHandler) {
            reduceState { State(isLoading = true) }
            listenMessages()
            val login = userInteractor.getUserLogin()
            reduceState { copy(login = login.orEmpty(), isLoading = false) }
        }
    }

    private fun sendMessage() {
        if (currentState.currentMessage.isBlank()) return

        coroutineScope.launch(coroutineExceptionHandler) {
            chatDetailsInteractor.sendMessage(currentState.currentMessage)
            reduceState { copy(currentMessage = "") }
        }
    }

    private fun listenMessages() {
        coroutineScope.launch {
            chatDetailsInteractor.listenMessages()
                .catch {
                    pushSideEffect(SideEffect.Error(errorHandler.handleError(it).defaultMessage))
                }.collect {
                    addMessage(it)
                }
        }
    }

    private fun addMessage(message: CommonChatMessage) {
        reduceState {
            copy(messages = messages + message.toState(currentState.login))
        }
    }
}