package ru.kabanchik.pro.feature.chat.internal.list

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
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
import ru.kabanchik.pro.domain.chat.logic.api.ProChatsListInteractor
import ru.kabanchik.pro.feature.chat.api.list.ProChatsListContract.Event
import ru.kabanchik.pro.feature.chat.api.list.ProChatsListContract.SideEffect
import ru.kabanchik.pro.feature.chat.internal.details.ProFakeErrorHandler
import ru.kabanchik.pro.feature.chat.internal.details.ProFakeUserInteractor
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs

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

    private fun runStoreTest(block: suspend TestScope.() -> Unit) = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        try {
            block()
        } finally {
            Dispatchers.resetMain()
        }
    }

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
    private class FakeProChatsListInteractor : ProChatsListInteractor {
        private val chats = mutableListOf<CommonChatSummary>()

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
