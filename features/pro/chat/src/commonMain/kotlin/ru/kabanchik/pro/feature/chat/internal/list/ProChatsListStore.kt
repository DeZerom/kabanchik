package ru.kabanchik.pro.feature.chat.internal.list

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.kabanchik.common.domain.user.logic.api.UserInteractor
import ru.kabanchik.common.errorHandler.logic.api.ErrorHandler
import ru.kabanchik.common.features.chat.logic.list.toUiChatItem
import ru.kabanchik.common.features.chat.logic.list.updateWithMessage
import ru.kabanchik.common.store.BaseCoroutineStore
import ru.kabanchik.pro.domain.chat.logic.api.ExecutorOffShiftException
import ru.kabanchik.pro.domain.chat.logic.api.ProChatsListInteractor
import ru.kabanchik.pro.feature.chat.api.list.ProChatsListContract.Event
import ru.kabanchik.pro.feature.chat.api.list.ProChatsListContract.SideEffect
import ru.kabanchik.pro.feature.chat.api.list.ProChatsListContract.State

class ProChatsListStore(
    private val proChatsListInteractor: ProChatsListInteractor,
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
            Event.RequestChat -> requestChat()
        }
    }

    private fun initChats() {
        listenReconnections()
        listenPausedChats()
        coroutineScope.launch(coroutineExceptionHandler) {
            reduceState { copy(isLoading = true) }

            proChatsListInteractor.connect()
            loadChats()
        }
    }

    private suspend fun loadChats() {
        val currentUserLogin = userInteractor.getUserLogin().orEmpty()
        val loadedChats = proChatsListInteractor.getChats().map { it.toUiChatItem() }
        reduceState { copy(chats = loadedChats, isLoading = false) }
        listenMessages(currentUserLogin)
    }

    private fun listenMessages(currentUserLogin: String) {
        if (messagesJob != null) return

        messagesJob = coroutineScope.launch(coroutineExceptionHandler) {
            proChatsListInteractor.listenMessages().collect { message ->
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
            proChatsListInteractor.listenReconnections().collect { reloadChats() }
        }
    }

    private suspend fun reloadChats() {
        try {
            loadChats()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Throwable) {
            pushSideEffect(SideEffect.ShowError(errorHandler.handleError(e).defaultMessage))
        }
    }

    // Чат снят с оператора при завершении смены
    private fun listenPausedChats() {
        coroutineScope.launch(coroutineExceptionHandler) {
            proChatsListInteractor.listenPausedChats().collect { sessionId ->
                reduceState { copy(chats = chats.filterNot { it.id == sessionId }) }
            }
        }
    }

    private fun requestChat() {
        if (currentState.isWaitingForClient) return

        coroutineScope.launch {
            try {
                reduceState { copy(isWaitingForClient = true) }
                val session = proChatsListInteractor.requestChat()
                reduceState { copy(isWaitingForClient = false) }
                pushSideEffect(SideEffect.NavigateDetails(session.sessionId))
                // Сообщения клиента пришли до принятия, поэтому сам по сообщениям чат в списке не появится
                coroutineScope.launch { reloadChats() }
            } catch (e: CancellationException) {
                throw e
            } catch (_: ExecutorOffShiftException) {
                // Без ошибки: домашний экран сам переключится на экран выхода на смену
                reduceState { copy(isWaitingForClient = false) }
            } catch (e: Throwable) {
                reduceState { copy(isWaitingForClient = false) }
                pushSideEffect(SideEffect.ShowError(errorHandler.handleError(e).defaultMessage))
            }
        }
    }
}
