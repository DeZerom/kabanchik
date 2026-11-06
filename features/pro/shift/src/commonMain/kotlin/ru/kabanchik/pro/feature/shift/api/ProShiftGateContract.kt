package ru.kabanchik.pro.feature.shift.api

import ru.kabanchik.common.tools.textResource.TextResource

class ProShiftGateContract {
    data class State(
        val isLoading: Boolean = true,
        val isError: Boolean = false,
        val isStartInProgress: Boolean = false,
        val isLogoutDialogVisible: Boolean = false,
        val isLogoutInProgress: Boolean = false
    )

    sealed interface Event {
        object ScreenStarted : Event
        object RetryClicked : Event
        object StartShiftClicked : Event
        object LogoutClicked : Event
        object LogoutConfirmed : Event
        object LogoutDismissed : Event
    }

    sealed interface SideEffect {
        class ShowError(val message: TextResource) : SideEffect
        object NavigateHome : SideEffect
        object NavigateAuth : SideEffect
    }
}
