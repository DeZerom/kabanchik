package ru.kabanchik.client.feature.auth.api.register

class RegisterContract {
    data class State(
        val login: String = "",
        val password: String = "",
        val isLoading: Boolean = false
    )

    sealed interface Event {
        class LoginChanged(val newLogin: String) : Event
        class PasswordChanged(val newPassword: String) : Event
        object CreateAccountClicked : Event
    }

    sealed interface SideEffect {
        class Error(val text: String) : SideEffect
    }
}