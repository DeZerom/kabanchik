package ru.kabanchik.feature.client.chatDetails.internal.list

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.kabanchik.client.domain.logic.chat.api.ClientChatsListInteractor
import ru.kabanchik.common.domain.user.logic.api.UserInteractor
import ru.kabanchik.common.errorHandler.logic.api.ErrorHandler
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
    private var messagesJob: Job? = null

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
        listenReconnections()
        coroutineScope.launch(coroutineExceptionHandler) {
            reduceState { copy(isLoading = true) }

            listInteractor.connect()
            loadChats()
        }
    }

    private suspend fun loadChats() {
        val currentUserLogin = userInteractor.getUserLogin().orEmpty()
        val loadedChats = listInteractor.getChats().map { it.toUiChatItem() }
        reduceState { copy(chats = loadedChats, isLoading = false) }
        listenMessages(currentUserLogin)
    }

    private fun listenMessages(currentUserLogin: String) {
        if (messagesJob != null) return

        messagesJob = coroutineScope.launch(coroutineExceptionHandler) {
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
    }

    // После переподключения сокета дозагружаем то, что могло прийти, пока соединения не было
    private fun listenReconnections() {
        coroutineScope.launch {
            listInteractor.listenReconnections().collect {
                try {
                    loadChats()
                } catch (e: CancellationException) {
                    throw e
                } catch (e: Throwable) {
                    pushSideEffect(SideEffect.ShowError(errorHandler.handleError(e).defaultMessage))
                }
            }
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
