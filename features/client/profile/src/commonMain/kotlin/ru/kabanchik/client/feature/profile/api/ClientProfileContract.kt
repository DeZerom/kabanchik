package ru.kabanchik.client.feature.profile.api

import ru.kabanchik.common.tools.textResource.TextResource

class ClientProfileContract {
    data class State(
        val login: String = "",
        val isLogoutDialogVisible: Boolean = false,
        val isLogoutInProgress: Boolean = false
    )

    sealed interface Event {
        object LogoutClicked : Event
        object LogoutConfirmed : Event
        object LogoutDismissed : Event
    }

    sealed interface SideEffect {
        class ShowError(val message: TextResource) : SideEffect
        object NavigateAuth : SideEffect
    }
}
