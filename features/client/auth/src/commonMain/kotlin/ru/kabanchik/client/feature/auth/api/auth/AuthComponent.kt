package ru.kabanchik.client.feature.auth.api.auth

import kotlinx.coroutines.flow.StateFlow

interface AuthComponent {
    val state: StateFlow<AuthContract.State>

    fun onLoginChanged(newLogin: String)
    fun onPasswordChanged(newPassword: String)
    fun onAuthorizeClicked()
    fun onCreateAccountClicked()
}