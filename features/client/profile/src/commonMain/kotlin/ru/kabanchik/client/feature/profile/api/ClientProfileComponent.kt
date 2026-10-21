package ru.kabanchik.client.feature.profile.api

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.StateFlow
import ru.kabanchik.client.feature.profile.internal.DefaultClientProfileComponent
import ru.kabanchik.common.snackBar.api.SnackBarData

interface ClientProfileComponent {
    val state: StateFlow<ClientProfileContract.State>
    fun onLogoutClicked()
    fun onLogoutConfirmed()
    fun onLogoutDismissed()

    companion object {
        fun create(
            componentContext: ComponentContext,
            dependencies: ClientProfileDependencies,
            showSnackBar: (SnackBarData) -> Unit,
            navigateAuth: () -> Unit
        ): ClientProfileComponent {
            return DefaultClientProfileComponent(
                componentContext = componentContext,
                dependencies = dependencies,
                showSnackBar = showSnackBar,
                navigateAuth = navigateAuth
            )
        }
    }
}
