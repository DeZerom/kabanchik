package ru.kabanchik.pro.feature.chat.internal.end

import kabanchik.features.pro.chat.generated.resources.Res
import kabanchik.features.pro.chat.generated.resources.chat_end_error
import kabanchik.features.pro.chat.generated.resources.chat_end_off_shift_error
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import ru.kabanchik.common.chat.model.CommonChatErrorCode
import ru.kabanchik.common.chat.model.CommonChatResolution
import ru.kabanchik.common.tools.textResource.TextResource
import ru.kabanchik.pro.domain.chat.logic.api.ExecutorOffShiftException
import ru.kabanchik.pro.domain.chat.logic.api.ProEndChatException
import ru.kabanchik.pro.feature.chat.api.end.ProEndChatContract.Event
import ru.kabanchik.pro.feature.chat.api.end.ProEndChatContract.SideEffect
import ru.kabanchik.pro.feature.chat.internal.details.FakeProChatDetailsInteractor
import ru.kabanchik.pro.feature.chat.internal.details.ProFakeErrorHandler
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class ProEndChatStoreTest {
    @Test
    fun offersClosedResolutionByDefault() = runStoreTest {
        val store = createStore(FakeProChatDetailsInteractor(upload = { error("Not used") }))

        assertEquals(listOf(CommonChatResolution.Closed), store.currentState.resolutions)
        assertEquals(CommonChatResolution.Closed, store.currentState.selectedResolution)
        assertFalse(store.currentState.isEnding)
        store.onDestroy()
    }

    @Test
    fun endsChatAndFinishes() = runStoreTest {
        val interactor = FakeProChatDetailsInteractor(upload = { error("Not used") })
        val store = createStore(interactor)

        store.handleEvent(Event.EndClicked)
        advanceUntilIdle()

        assertEquals(SessionId, interactor.endedSessionId)
        assertIs<SideEffect.Finished>(store.sideEffect.first())
        assertFalse(store.currentState.isEnding)
        store.onDestroy()
    }

    @Test
    fun showsProgressAndIgnoresRepeatedClicksWhileEnding() = runStoreTest {
        val allowEnding = CompletableDeferred<Unit>()
        var endCalls = 0
        val store = createStore(
            FakeProChatDetailsInteractor(
                upload = { error("Not used") },
                end = {
                    endCalls++
                    allowEnding.await()
                }
            )
        )

        store.handleEvent(Event.EndClicked)
        runCurrent()
        store.handleEvent(Event.EndClicked)
        runCurrent()

        assertTrue(store.currentState.isEnding)
        assertEquals(1, endCalls)
        allowEnding.complete(Unit)
        advanceUntilIdle()
        assertFalse(store.currentState.isEnding)
        store.onDestroy()
    }

    @Test
    fun showsErrorWhenChatWasNotEnded() = runStoreTest {
        val interactor = FakeProChatDetailsInteractor(
            upload = { error("Not used") },
            end = { throw ProEndChatException(CommonChatErrorCode.SessionEndFailed) }
        )
        val store = createStore(interactor)

        store.handleEvent(Event.EndClicked)
        advanceUntilIdle()

        val effect = assertIs<SideEffect.Error>(store.sideEffect.first())
        assertEquals(Res.string.chat_end_error, (effect.text as TextResource.Id).value)
        assertNull(interactor.endedSessionId)
        assertFalse(store.currentState.isEnding)
        store.onDestroy()
    }

    @Test
    fun showsOffShiftError() = runStoreTest {
        val store = createStore(
            FakeProChatDetailsInteractor(
                upload = { error("Not used") },
                end = { throw ExecutorOffShiftException() }
            )
        )

        store.handleEvent(Event.EndClicked)
        advanceUntilIdle()

        val effect = assertIs<SideEffect.Error>(store.sideEffect.first())
        assertEquals(Res.string.chat_end_off_shift_error, (effect.text as TextResource.Id).value)
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

    private fun createStore(interactor: FakeProChatDetailsInteractor): ProEndChatStore {
        return ProEndChatStore(
            chatDetailsInteractor = interactor,
            errorHandler = ProFakeErrorHandler,
            sessionId = SessionId,
        )
    }

    private companion object {
        const val SessionId = "session-id"
    }
}
