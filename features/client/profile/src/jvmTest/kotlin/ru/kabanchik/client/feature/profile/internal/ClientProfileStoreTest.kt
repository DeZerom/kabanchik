package ru.kabanchik.client.feature.profile.internal

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
import ru.kabanchik.client.feature.profile.api.ClientProfileContract.Event
import ru.kabanchik.client.feature.profile.api.ClientProfileContract.SideEffect
import ru.kabanchik.common.domain.profile.logic.api.CommonProfileInteractor
import ru.kabanchik.common.errorHandler.logic.api.ErrorHandler
import ru.kabanchik.common.errorHandler.logic.api.ErrorType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class ClientProfileStoreTest {
    @Test
    fun loadsUserLogin() = runStoreTest {
        val store = createStore(FakeProfileInteractor(login = "user"))

        assertEquals("user", store.currentState.login)
        store.onDestroy()
    }

    @Test
    fun showsAndHidesLogoutDialog() = runStoreTest {
        val store = createStore(FakeProfileInteractor())

        store.handleEvent(Event.LogoutClicked)
        assertTrue(store.currentState.isLogoutDialogVisible)

        store.handleEvent(Event.LogoutDismissed)
        assertFalse(store.currentState.isLogoutDialogVisible)
        store.onDestroy()
    }

    @Test
    fun logsOutAndNavigatesAuthOnConfirm() = runStoreTest {
        val interactor = FakeProfileInteractor()
        val store = createStore(interactor)

        store.handleEvent(Event.LogoutClicked)
        store.handleEvent(Event.LogoutConfirmed)
        advanceUntilIdle()

        assertEquals(1, interactor.logoutCalls)
        assertIs<SideEffect.NavigateAuth>(store.sideEffect.first())
        store.onDestroy()
    }

    @Test
    fun keepsDialogOpenWhileLogoutInProgress() = runStoreTest {
        val logoutResult = CompletableDeferred<Unit>()
        val interactor = FakeProfileInteractor(logoutResult = logoutResult)
        val store = createStore(interactor)

        store.handleEvent(Event.LogoutClicked)
        store.handleEvent(Event.LogoutConfirmed)
        runCurrent()
        store.handleEvent(Event.LogoutDismissed)
        store.handleEvent(Event.LogoutConfirmed)
        runCurrent()

        assertTrue(store.currentState.isLogoutDialogVisible)
        assertTrue(store.currentState.isLogoutInProgress)
        assertEquals(1, interactor.logoutCalls)

        logoutResult.complete(Unit)
        advanceUntilIdle()
        store.onDestroy()
    }

    @Test
    fun showsErrorWhenLogoutFails() = runStoreTest {
        val store = createStore(FakeProfileInteractor(logoutError = IllegalStateException()))

        store.handleEvent(Event.LogoutClicked)
        store.handleEvent(Event.LogoutConfirmed)
        advanceUntilIdle()

        assertFalse(store.currentState.isLogoutInProgress)
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

    private fun TestScope.createStore(interactor: FakeProfileInteractor): ClientProfileStore {
        val store = ClientProfileStore(
            profileInteractor = interactor,
            errorHandler = FakeErrorHandler
        )
        runCurrent()
        return store
    }

    private class FakeProfileInteractor(
        private val login: String? = null,
        private val logoutResult: CompletableDeferred<Unit>? = null,
        private val logoutError: Throwable? = null
    ) : CommonProfileInteractor {
        var logoutCalls = 0
            private set

        override suspend fun getUserLogin(): String? {
            return login
        }

        override suspend fun logout() {
            logoutCalls++
            logoutResult?.await()
            logoutError?.let { throw it }
        }
    }

    private object FakeErrorHandler : ErrorHandler {
        override fun handleError(error: Throwable): ErrorType = ErrorType.Unknown
    }
}
