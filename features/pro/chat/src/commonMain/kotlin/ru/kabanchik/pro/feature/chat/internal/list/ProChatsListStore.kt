package ru.kabanchik.pro.feature.chat.internal.list

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import ru.kabanchik.common.errorHandler.logic.api.ErrorHandler
import ru.kabanchik.common.store.BaseCoroutineStore
import ru.kabanchik.pro.domain.chat.logic.api.ProChatsListInteractor
import ru.kabanchik.pro.feature.chat.api.list.ProChatsListContract.Event
import ru.kabanchik.pro.feature.chat.api.list.ProChatsListContract.SideEffect
import ru.kabanchik.pro.feature.chat.api.list.ProChatsListContract.State

class ProChatsListStore(
    private val proChatsListInteractor: ProChatsListInteractor,
    private val errorHandler: ErrorHandler
) : BaseCoroutineStore<Event, State, SideEffect>() {
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        pushSideEffect(SideEffect.ShowError(errorHandler.handleError(throwable).defaultMessage))
    }

    init {
        initChats()
    }

    override fun initState(): State {
        return State()
    }

    override fun handleEvent(event: Event) {
        when (event) {
            Event.RequestChat -> requestChat()
        }
    }

    private fun initChats() {
        coroutineScope.launch(coroutineExceptionHandler) {
            reduceState { copy(isLoading = true) }
            proChatsListInteractor.connect()
        }.invokeOnCompletion {
            reduceState { copy(isLoading = false) }
        }
    }

    private fun requestChat() {
        coroutineScope.launch {
            runCatching {
                reduceState { copy(isWaitingForClient = true) }
                proChatsListInteractor.requestChat()
                pushSideEffect(SideEffect.NavigateDetails)
            }.onFailure {
                reduceState { copy(isWaitingForClient = false) }
                pushSideEffect(SideEffect.ShowError(errorHandler.handleError(it).defaultMessage))
            }
        }
    }
}