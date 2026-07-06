package ru.kabanchik.client.feature.auth.api.register

import ru.kabanchik.common.tools.textResource.TextResource

class RegisterContract {
    data class State(
        val login: String = "",
        val password: String = "",
        val isLoading: Boolean = false,
        val isPasswordVisible: Boolean = false
    )

    sealed interface Event {
        class LoginChanged(val newLogin: String) : Event
        class PasswordChanged(val newPassword: String) : Event
        object CreateAccountClicked : Event
        object ChangePasswordVisibility : Event
    }

    sealed interface SideEffect {
        class Error(val text: TextResource) : SideEffect
        object Success : SideEffect
    }
}
