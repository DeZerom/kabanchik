package ru.kabanchik.pro.feature.profile.internal

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import ru.kabanchik.common.domain.profile.logic.api.CommonProfileInteractor
import ru.kabanchik.common.errorHandler.logic.api.ErrorHandler
import ru.kabanchik.common.store.BaseCoroutineStore
import ru.kabanchik.pro.feature.profile.api.ProProfileContract.Event
import ru.kabanchik.pro.feature.profile.api.ProProfileContract.SideEffect
import ru.kabanchik.pro.feature.profile.api.ProProfileContract.State

internal class ProProfileStore(
    private val profileInteractor: CommonProfileInteractor,
    private val errorHandler: ErrorHandler
) : BaseCoroutineStore<Event, State, SideEffect>() {
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        reduceState { copy(isLogoutInProgress = false) }
        pushSideEffect(SideEffect.ShowError(errorHandler.handleError(throwable).defaultMessage))
    }

    init {
        loadLogin()
    }

    override fun initState(): State {
        return State()
    }

    override fun handleEvent(event: Event) {
        when (event) {
            Event.LogoutClicked -> reduceState { copy(isLogoutDialogVisible = true) }
            Event.LogoutConfirmed -> logout()
            Event.LogoutDismissed -> dismissLogoutDialog()
        }
    }

    private fun loadLogin() {
        coroutineScope.launch(coroutineExceptionHandler) {
            val login = profileInteractor.getUserLogin().orEmpty()
            reduceState { copy(login = login) }
        }
    }

    private fun dismissLogoutDialog() {
        if (currentState.isLogoutInProgress) return

        reduceState { copy(isLogoutDialogVisible = false) }
    }

    private fun logout() {
        if (currentState.isLogoutInProgress) return

        reduceState { copy(isLogoutInProgress = true) }
        coroutineScope.launch(coroutineExceptionHandler) {
            profileInteractor.logout()
            pushSideEffect(SideEffect.NavigateAuth)
        }
    }
}
