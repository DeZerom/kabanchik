package ru.kabanchik.pro.feature.profile.api

import ru.kabanchik.common.tools.textResource.TextResource

class ProProfileContract {
    data class State(
        val login: String = "",
        val isLogoutDialogVisible: Boolean = false,
        val isLogoutInProgress: Boolean = false,
        val isEndShiftDialogVisible: Boolean = false,
        val isEndShiftInProgress: Boolean = false
    )

    sealed interface Event {
        object LogoutClicked : Event
        object LogoutConfirmed : Event
        object LogoutDismissed : Event
        object EndShiftClicked : Event
        object EndShiftConfirmed : Event
        object EndShiftDismissed : Event
    }

    sealed interface SideEffect {
        class ShowError(val message: TextResource) : SideEffect
        object NavigateAuth : SideEffect
    }
}
