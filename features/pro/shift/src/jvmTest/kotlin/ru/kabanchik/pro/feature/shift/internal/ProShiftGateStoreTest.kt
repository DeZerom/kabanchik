package ru.kabanchik.pro.feature.shift.internal

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import ru.kabanchik.common.errorHandler.logic.api.ErrorHandler
import ru.kabanchik.common.errorHandler.logic.api.ErrorType
import ru.kabanchik.pro.domain.shift.logic.api.ProShiftInteractor
import ru.kabanchik.pro.domain.shift.model.ProShiftStatus
import ru.kabanchik.pro.feature.shift.api.ProShiftGateContract.Event
import ru.kabanchik.pro.feature.shift.api.ProShiftGateContract.SideEffect
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class ProShiftGateStoreTest {
    @Test
    fun navigatesHomeWhenAlreadyOnShift() = runStoreTest {
        val store = createStore(FakeShiftInteractor(serverStatus = ProShiftStatus.OnShift))

        assertIs<SideEffect.NavigateHome>(store.sideEffect.first())
        store.onDestroy()
    }

    @Test
    fun showsStartButtonWhenOffShift() = runStoreTest {
        val store = createStore(FakeShiftInteractor(serverStatus = ProShiftStatus.OffShift))

        assertFalse(store.currentState.isLoading)
        assertFalse(store.currentState.isError)
        store.onDestroy()
    }

    @Test
    fun showsErrorAndRetriesWhenRefreshFails() = runStoreTest {
        val interactor = FakeShiftInteractor(
            serverStatus = ProShiftStatus.OffShift,
            refreshError = IllegalStateException()
        )
        val store = createStore(interactor)

        assertFalse(store.currentState.isLoading)
        assertTrue(store.currentState.isError)

        interactor.refreshError = null
        store.handleEvent(Event.RetryClicked)
        advanceUntilIdle()

        assertFalse(store.currentState.isError)
        assertEquals(2, interactor.refreshCalls)
        store.onDestroy()
    }

    @Test
    fun startsShiftAndNavigatesHome() = runStoreTest {
        val interactor = FakeShiftInteractor(serverStatus = ProShiftStatus.OffShift)
        val store = createStore(interactor)

        store.handleEvent(Event.StartShiftClicked)
        store.handleEvent(Event.StartShiftClicked)
        advanceUntilIdle()

        assertEquals(1, interactor.startCalls)
        assertIs<SideEffect.NavigateHome>(store.sideEffect.first())
        store.onDestroy()
    }

    @Test
    fun showsErrorWhenStartFails() = runStoreTest {
        val interactor = FakeShiftInteractor(
            serverStatus = ProShiftStatus.OffShift,
            startError = IllegalStateException()
        )
        val store = createStore(interactor)

        store.handleEvent(Event.StartShiftClicked)
        advanceUntilIdle()

        assertFalse(store.currentState.isStartInProgress)
        assertIs<SideEffect.ShowError>(store.sideEffect.first())
        store.onDestroy()
    }

    @Test
    fun navigatesHomeWhenShiftStartedOnAnotherDevice() = runStoreTest {
        val interactor = FakeShiftInteractor(serverStatus = ProShiftStatus.OffShift)
        val store = createStore(interactor)

        interactor.serverStatus = ProShiftStatus.OnShift
        store.handleEvent(Event.ScreenStarted)
        advanceUntilIdle()

        assertIs<SideEffect.NavigateHome>(store.sideEffect.first())
        store.onDestroy()
    }

    @Test
    fun logsOutAndNavigatesAuth() = runStoreTest {
        val interactor = FakeShiftInteractor(serverStatus = ProShiftStatus.OffShift)
        val store = createStore(interactor)

        store.handleEvent(Event.LogoutClicked)
        assertTrue(store.currentState.isLogoutDialogVisible)
        store.handleEvent(Event.LogoutConfirmed)
        advanceUntilIdle()

        assertEquals(1, interactor.logoutCalls)
        assertIs<SideEffect.NavigateAuth>(store.sideEffect.first())
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

    private fun TestScope.createStore(interactor: FakeShiftInteractor): ProShiftGateStore {
        val store = ProShiftGateStore(
            shiftInteractor = interactor,
            errorHandler = FakeErrorHandler
        )
        advanceUntilIdle()
        runCurrent()
        return store
    }

    private class FakeShiftInteractor(
        var serverStatus: ProShiftStatus,
        var refreshError: Throwable? = null,
        private val startError: Throwable? = null
    ) : ProShiftInteractor {
        var refreshCalls = 0
            private set
        var startCalls = 0
            private set
        var logoutCalls = 0
            private set

        private val _shiftStatus = MutableStateFlow<ProShiftStatus?>(null)
        override val shiftStatus: StateFlow<ProShiftStatus?> = _shiftStatus

        override suspend fun refresh(): ProShiftStatus {
            refreshCalls++
            refreshError?.let { throw it }
            _shiftStatus.value = serverStatus
            return serverStatus
        }

        override suspend fun startShift(): ProShiftStatus {
            startCalls++
            startError?.let { throw it }
            serverStatus = ProShiftStatus.OnShift
            _shiftStatus.value = serverStatus
            return serverStatus
        }

        override suspend fun endShift(): ProShiftStatus {
            serverStatus = ProShiftStatus.OffShift
            _shiftStatus.value = serverStatus
            return serverStatus
        }

        override suspend fun logout() {
            logoutCalls++
        }

        override fun markOffShift() {
            _shiftStatus.value = ProShiftStatus.OffShift
        }

        override fun clear() {
            _shiftStatus.value = null
        }
    }

    private object FakeErrorHandler : ErrorHandler {
        override fun handleError(error: Throwable): ErrorType = ErrorType.Unknown
    }
}
