package ru.kabanchik.pro.feature.chat.internal.list

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import ru.kabanchik.common.chat.model.CommonChatSummary
import ru.kabanchik.common.chat.model.CommonMessage
import ru.kabanchik.common.chat.model.CommonSessionMessage
import ru.kabanchik.common.chat.model.CommonSessionStatus
import ru.kabanchik.common.feature.chat.model.CommonChatListTab
import ru.kabanchik.pro.domain.chat.logic.api.ProChatsListInteractor
import ru.kabanchik.pro.feature.chat.api.list.ProChatsListContract.Event
import ru.kabanchik.pro.feature.chat.api.list.ProChatsListContract.SideEffect
import ru.kabanchik.pro.feature.chat.internal.details.ProFakeErrorHandler
import ru.kabanchik.pro.feature.chat.internal.details.ProFakeUserInteractor
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class ProChatsListStoreTest {
    @Test
    fun showsAcceptedChatInList() = runStoreTest {
        val interactor = FakeProChatsListInteractor()
        val store = createStore(interactor)
        assertEquals(emptyList(), store.currentState.chats)

        store.handleEvent(Event.RequestChat)
        advanceUntilIdle()

        assertIs<SideEffect.NavigateDetails>(store.sideEffect.first())
        assertEquals(listOf("session-1"), store.currentState.chats.map { it.id })
        assertFalse(store.currentState.isWaitingForClient)
        store.onDestroy()
    }

    @Test
    fun loadsClosedChatsWhenClosedTabIsSelected() = runStoreTest {
        val interactor = FakeProChatsListInteractor(closedChats = listOf(closedChat("closed-1")))
        val store = createStore(interactor)
        assertEquals(0, interactor.closedChatsRequests)

        store.handleEvent(Event.TabSelected(CommonChatListTab.Closed))
        advanceUntilIdle()

        assertEquals(CommonChatListTab.Closed, store.currentState.selectedTab)
        assertEquals(listOf("closed-1"), store.currentState.closedChats.map { it.id })
        assertTrue(store.currentState.isClosedLoaded)
        assertFalse(store.currentState.isClosedLoading)
        store.onDestroy()
    }

    @Test
    fun showsEmptyClosedListWhenThereAreNoClosedChats() = runStoreTest {
        val store = createStore(FakeProChatsListInteractor())

        store.handleEvent(Event.TabSelected(CommonChatListTab.Closed))
        advanceUntilIdle()

        assertTrue(store.currentState.closedChats.isEmpty())
        assertTrue(store.currentState.isClosedLoaded)
        store.onDestroy()
    }

    @Test
    fun movesEndedChatFromActiveToClosed() = runStoreTest {
        val interactor = FakeProChatsListInteractor(activeChats = listOf(activeChat("session-1")))
        val store = createStore(interactor)
        store.handleEvent(Event.TabSelected(CommonChatListTab.Closed))
        advanceUntilIdle()
        assertEquals(listOf("session-1"), store.currentState.chats.map { it.id })

        interactor.closeChat("session-1")
        advanceUntilIdle()

        assertTrue(store.currentState.chats.isEmpty())
        assertEquals(listOf("session-1"), store.currentState.closedChats.map { it.id })
        assertEquals(2, interactor.closedChatsRequests)
        assertTrue(store.currentState.closedChats.single().resolution != null)
        store.onDestroy()
    }

    @Test
    fun showsErrorWhenClosedChatsFailToLoad() = runStoreTest {
        val interactor = FakeProChatsListInteractor(closedChatsError = IllegalStateException("No network"))
        val store = createStore(interactor)

        store.handleEvent(Event.TabSelected(CommonChatListTab.Closed))
        advanceUntilIdle()

        assertIs<SideEffect.ShowError>(store.sideEffect.first())
        assertFalse(store.currentState.isClosedLoading)
        assertFalse(store.currentState.isClosedLoaded)
        store.onDestroy()
    }

    private fun runStoreTest(block: suspend TestScope.() -> Unit) = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        try {
            block()
        } finally {
            Dispatchers.resetMain()
        }
    }

    private fun activeChat(sessionId: String): CommonChatSummary = CommonChatSummary(
        sessionId = sessionId,
        status = CommonSessionStatus.Open,
        participantName = "user_77",
        firstMessageContent = "Помогите",
        lastMessageContent = "Помогите",
        lastMessageTimestamp = null,
        lastMessageSender = "user_77"
    )

    private fun closedChat(sessionId: String): CommonChatSummary =
        activeChat(sessionId).copy(status = CommonSessionStatus.Closed)

    private fun TestScope.createStore(interactor: FakeProChatsListInteractor): ProChatsListStore {
        val store = ProChatsListStore(
            proChatsListInteractor = interactor,
            userInteractor = ProFakeUserInteractor,
            errorHandler = ProFakeErrorHandler
        )
        advanceUntilIdle()
        return store
    }

    /**
     * Сервер отдает принятый чат в списке только после [requestChat].
     */
    private class FakeProChatsListInteractor(
        activeChats: List<CommonChatSummary> = emptyList(),
        closedChats: List<CommonChatSummary> = emptyList(),
        private val closedChatsError: Throwable? = null,
    ) : ProChatsListInteractor {
        private val chats = activeChats.toMutableList()
        private val closedChats = closedChats.toMutableList()
        private val closedEvents = MutableSharedFlow<String>(extraBufferCapacity = 16)
        var closedChatsRequests = 0

        suspend fun closeChat(sessionId: String) {
            val chat = chats.first { it.sessionId == sessionId }
            chats -= chat
            closedChats.add(0, chat.copy(status = CommonSessionStatus.Closed))
            closedEvents.emit(sessionId)
        }

        override suspend fun getClosedChats(): List<CommonChatSummary> {
            closedChatsRequests++
            closedChatsError?.let { throw it }
            return closedChats.toList()
        }

        override suspend fun listenClosedChats(): Flow<String> = closedEvents

        override suspend fun connect() = Unit

        override suspend fun listenReconnections(): Flow<Unit> = emptyFlow()

        override suspend fun getChats(): List<CommonChatSummary> = chats.toList()

        override suspend fun listenMessages(): Flow<CommonMessage> = emptyFlow()

        override suspend fun requestChat(): CommonSessionMessage {
            chats += CommonChatSummary(
                sessionId = "session-1",
                status = CommonSessionStatus.Open,
                participantName = "user_77",
                firstMessageContent = "Помогите",
                lastMessageContent = "Помогите",
                lastMessageTimestamp = null,
                lastMessageSender = "user_77"
            )
            return CommonSessionMessage(sessionId = "session-1", status = CommonSessionStatus.Open)
        }

        override suspend fun listenPausedChats(): Flow<String> = emptyFlow()
    }
}
