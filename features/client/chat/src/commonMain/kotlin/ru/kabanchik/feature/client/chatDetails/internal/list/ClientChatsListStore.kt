package ru.kabanchik.feature.client.chatDetails.internal.list

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import ru.kabanchik.client.domain.logic.chat.api.ClientChatsListInteractor
import ru.kabanchik.common.errorHandler.logic.api.ErrorHandler
import ru.kabanchik.common.domain.user.logic.api.UserInteractor
import ru.kabanchik.common.features.chat.logic.list.toUiChatItem
import ru.kabanchik.common.features.chat.logic.list.updateWithMessage
import ru.kabanchik.common.store.BaseCoroutineStore
import ru.kabanchik.feature.client.chatDetails.api.list.ClientChatsListContract.Event
import ru.kabanchik.feature.client.chatDetails.api.list.ClientChatsListContract.SideEffect
import ru.kabanchik.feature.client.chatDetails.api.list.ClientChatsListContract.State

internal class ClientChatsListStore(
    private val listInteractor: ClientChatsListInteractor,
    private val userInteractor: UserInteractor,
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
            Event.CreateChatClicked -> createChat()
        }
    }

    private fun initChats() {
        coroutineScope.launch(coroutineExceptionHandler) {
            reduceState { copy(isLoading = true) }

            listInteractor.connect()
            val currentUserLogin = userInteractor.getUserLogin().orEmpty()
            val loadedChats = listInteractor.getChats().map { it.toUiChatItem() }
            reduceState { copy(chats = loadedChats) }

            launch {
                listInteractor.listenMessages().collect { message ->
                    reduceState {
                        copy(
                            chats = this.chats.updateWithMessage(
                                message = message,
                                currentUserLogin = currentUserLogin,
                            )
                        )
                    }
                }
            }

            reduceState { copy(isLoading = false) }
        }
    }

    private fun createChat() {
        coroutineScope.launch(coroutineExceptionHandler) {
            reduceState { copy(isChatCreating = true) }
            try {
                val session = listInteractor.createChat()
                pushSideEffect(SideEffect.NavigateChatDetails(session.sessionId))
            } finally {
                reduceState { copy(isChatCreating = false) }
            }
        }
    }
}
