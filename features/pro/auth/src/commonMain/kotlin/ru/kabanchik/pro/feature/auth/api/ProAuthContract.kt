package ru.kabanchik.pro.feature.auth.api

import ru.kabanchik.common.tools.textResource.TextResource

class ProAuthContract {
    data class State(
        val login: String = "",
        val password: String = "",
        val isLoading: Boolean = false
    )

    sealed interface Event {
        class LoginChanged(val newLogin: String) : Event
        class PasswordChanged(val newPassword: String) : Event
        object AuthorizeClicked : Event
    }

    sealed interface SideEffect {
        object Authorized : SideEffect
        class Error(val text: TextResource) : SideEffect
    }
}