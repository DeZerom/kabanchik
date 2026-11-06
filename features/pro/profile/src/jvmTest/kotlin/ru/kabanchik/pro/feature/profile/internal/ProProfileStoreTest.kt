package ru.kabanchik.pro.feature.profile.internal

import kotlinx.coroutines.CompletableDeferred
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
import ru.kabanchik.common.domain.profile.logic.api.CommonProfileInteractor
import ru.kabanchik.common.errorHandler.logic.api.ErrorHandler
import ru.kabanchik.common.errorHandler.logic.api.ErrorType
import ru.kabanchik.pro.domain.shift.logic.api.ProShiftInteractor
import ru.kabanchik.pro.domain.shift.model.ProShiftStatus
import ru.kabanchik.pro.feature.profile.api.ProProfileContract.Event
import ru.kabanchik.pro.feature.profile.api.ProProfileContract.SideEffect
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class ProProfileStoreTest {
    @Test
    fun loadsUserLogin() = runStoreTest {
        val store = createStore(profileInteractor = FakeProfileInteractor(login = "user"))

        assertEquals("user", store.currentState.login)
        store.onDestroy()
    }

    @Test
    fun showsAndHidesLogoutDialog() = runStoreTest {
        val store = createStore()

        store.handleEvent(Event.LogoutClicked)
        assertTrue(store.currentState.isLogoutDialogVisible)

        store.handleEvent(Event.LogoutDismissed)
        assertFalse(store.currentState.isLogoutDialogVisible)
        store.onDestroy()
    }

    @Test
    fun logsOutAndNavigatesAuthOnConfirm() = runStoreTest {
        val shiftInteractor = FakeShiftInteractor()
        val store = createStore(shiftInteractor = shiftInteractor)

        store.handleEvent(Event.LogoutClicked)
        store.handleEvent(Event.LogoutConfirmed)
        advanceUntilIdle()

        assertEquals(1, shiftInteractor.logoutCalls)
        assertIs<SideEffect.NavigateAuth>(store.sideEffect.first())
        store.onDestroy()
    }

    @Test
    fun keepsDialogOpenWhileLogoutInProgress() = runStoreTest {
        val logoutResult = CompletableDeferred<Unit>()
        val shiftInteractor = FakeShiftInteractor(logoutResult = logoutResult)
        val store = createStore(shiftInteractor = shiftInteractor)

        store.handleEvent(Event.LogoutClicked)
        store.handleEvent(Event.LogoutConfirmed)
        runCurrent()
        store.handleEvent(Event.LogoutDismissed)
        store.handleEvent(Event.LogoutConfirmed)
        runCurrent()

        assertTrue(store.currentState.isLogoutDialogVisible)
        assertTrue(store.currentState.isLogoutInProgress)
        assertEquals(1, shiftInteractor.logoutCalls)

        logoutResult.complete(Unit)
        advanceUntilIdle()
        store.onDestroy()
    }

    @Test
    fun showsErrorWhenLogoutFails() = runStoreTest {
        val store = createStore(shiftInteractor = FakeShiftInteractor(logoutError = IllegalStateException()))

        store.handleEvent(Event.LogoutClicked)
        store.handleEvent(Event.LogoutConfirmed)
        advanceUntilIdle()

        assertFalse(store.currentState.isLogoutInProgress)
        assertIs<SideEffect.ShowError>(store.sideEffect.first())
        store.onDestroy()
    }

    @Test
    fun showsAndHidesEndShiftDialog() = runStoreTest {
        val store = createStore()

        store.handleEvent(Event.EndShiftClicked)
        assertTrue(store.currentState.isEndShiftDialogVisible)

        store.handleEvent(Event.EndShiftDismissed)
        assertFalse(store.currentState.isEndShiftDialogVisible)
        store.onDestroy()
    }

    @Test
    fun endsShiftOnConfirm() = runStoreTest {
        val endResult = CompletableDeferred<Unit>()
        val shiftInteractor = FakeShiftInteractor(endResult = endResult)
        val store = createStore(shiftInteractor = shiftInteractor)

        store.handleEvent(Event.EndShiftClicked)
        store.handleEvent(Event.EndShiftConfirmed)
        runCurrent()
        store.handleEvent(Event.EndShiftDismissed)
        store.handleEvent(Event.EndShiftConfirmed)
        runCurrent()

        assertTrue(store.currentState.isEndShiftDialogVisible)
        assertTrue(store.currentState.isEndShiftInProgress)
        assertEquals(1, shiftInteractor.endShiftCalls)

        endResult.complete(Unit)
        advanceUntilIdle()

        assertFalse(store.currentState.isEndShiftInProgress)
        assertEquals(ProShiftStatus.OffShift, shiftInteractor.shiftStatus.value)
        store.onDestroy()
    }

    @Test
    fun showsErrorWhenEndShiftFails() = runStoreTest {
        val store = createStore(shiftInteractor = FakeShiftInteractor(endError = IllegalStateException()))

        store.handleEvent(Event.EndShiftClicked)
        store.handleEvent(Event.EndShiftConfirmed)
        advanceUntilIdle()

        assertFalse(store.currentState.isEndShiftInProgress)
        assertTrue(store.currentState.isEndShiftDialogVisible)
        assertIs<SideEffect.ShowError>(store.sideEffect.first())
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

    private fun TestScope.createStore(
        profileInteractor: FakeProfileInteractor = FakeProfileInteractor(),
        shiftInteractor: FakeShiftInteractor = FakeShiftInteractor()
    ): ProProfileStore {
        val store = ProProfileStore(
            profileInteractor = profileInteractor,
            shiftInteractor = shiftInteractor,
            errorHandler = FakeErrorHandler
        )
        runCurrent()
        return store
    }

    private class FakeProfileInteractor(
        private val login: String? = null
    ) : CommonProfileInteractor {
        override suspend fun getUserLogin(): String? {
            return login
        }

        override suspend fun logout() = Unit
    }

    private class FakeShiftInteractor(
        private val logoutResult: CompletableDeferred<Unit>? = null,
        private val logoutError: Throwable? = null,
        private val endResult: CompletableDeferred<Unit>? = null,
        private val endError: Throwable? = null
    ) : ProShiftInteractor {
        var logoutCalls = 0
            private set
        var endShiftCalls = 0
            private set

        private val _shiftStatus = MutableStateFlow<ProShiftStatus?>(ProShiftStatus.OnShift)
        override val shiftStatus: StateFlow<ProShiftStatus?> = _shiftStatus

        override suspend fun refresh(): ProShiftStatus = checkNotNull(_shiftStatus.value)

        override suspend fun startShift(): ProShiftStatus {
            _shiftStatus.value = ProShiftStatus.OnShift
            return ProShiftStatus.OnShift
        }

        override suspend fun endShift(): ProShiftStatus {
            endShiftCalls++
            endResult?.await()
            endError?.let { throw it }
            _shiftStatus.value = ProShiftStatus.OffShift
            return ProShiftStatus.OffShift
        }

        override suspend fun logout() {
            logoutCalls++
            logoutResult?.await()
            logoutError?.let { throw it }
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
