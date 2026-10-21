package ru.kabanchik.pro.feature.shift.internal

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import ru.kabanchik.common.errorHandler.logic.api.ErrorHandler
import ru.kabanchik.common.store.BaseCoroutineStore
import ru.kabanchik.pro.domain.shift.logic.api.ProShiftInteractor
import ru.kabanchik.pro.domain.shift.model.ProShiftStatus
import ru.kabanchik.pro.feature.shift.api.ProShiftGateContract.Event
import ru.kabanchik.pro.feature.shift.api.ProShiftGateContract.SideEffect
import ru.kabanchik.pro.feature.shift.api.ProShiftGateContract.State

internal class ProShiftGateStore(
    private val shiftInteractor: ProShiftInteractor,
    private val errorHandler: ErrorHandler
) : BaseCoroutineStore<Event, State, SideEffect>() {
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        reduceState { copy(isStartInProgress = false, isLogoutInProgress = false) }
        pushSideEffect(SideEffect.ShowError(errorHandler.handleError(throwable).defaultMessage))
    }

    init {
        loadShift()
    }

    override fun initState(): State {
        return State()
    }

    override fun handleEvent(event: Event) {
        when (event) {
            Event.ScreenStarted -> refreshSilently()
            Event.RetryClicked -> loadShift()
            Event.StartShiftClicked -> startShift()
            Event.LogoutClicked -> reduceState { copy(isLogoutDialogVisible = true) }
            Event.LogoutConfirmed -> logout()
            Event.LogoutDismissed -> dismissLogoutDialog()
        }
    }

    private fun loadShift() {
        reduceState { copy(isLoading = true, isError = false) }
        coroutineScope.launch {
            try {
                onShiftStatus(shiftInteractor.refresh())
            } catch (e: CancellationException) {
                throw e
            } catch (e: Throwable) {
                errorHandler.handleError(e)
                reduceState { copy(isLoading = false, isError = true) }
            }
        }
    }

    private fun refreshSilently() {
        if (currentState.isLoading || currentState.isStartInProgress) return

        coroutineScope.launch {
            try {
                onShiftStatus(shiftInteractor.refresh())
            } catch (e: CancellationException) {
                throw e
            } catch (e: Throwable) {
                // Фоновая проверка: экран остается в текущем состоянии
                errorHandler.handleError(e)
            }
        }
    }

    private fun onShiftStatus(status: ProShiftStatus) {
        when (status) {
            ProShiftStatus.OnShift -> pushSideEffect(SideEffect.NavigateHome)
            ProShiftStatus.OffShift -> reduceState { copy(isLoading = false, isError = false) }
        }
    }

    private fun startShift() {
        if (currentState.isStartInProgress) return

        reduceState { copy(isStartInProgress = true) }
        coroutineScope.launch(coroutineExceptionHandler) {
            shiftInteractor.startShift()
            pushSideEffect(SideEffect.NavigateHome)
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
            shiftInteractor.logout()
            pushSideEffect(SideEffect.NavigateAuth)
        }
    }
}
