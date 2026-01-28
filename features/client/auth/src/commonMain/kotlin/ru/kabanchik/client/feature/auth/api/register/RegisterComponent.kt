package ru.kabanchik.client.feature.auth.api.register

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface RegisterComponent {
    val state: StateFlow<RegisterContract.State>
    val messagesFlow: Flow<String>

    fun onLoginChanged(newLogin: String)
    fun onPasswordChanged(newPassword: String)
    fun onCreateAccountClicked()
    fun onHaveAccountClicked()
}