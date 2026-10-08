package ru.kabanchik.pro.feature.profile.api

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.StateFlow
import ru.kabanchik.common.snackBar.api.SnackBarData
import ru.kabanchik.pro.feature.profile.internal.DefaultProProfileComponent

interface ProProfileComponent {
    val state: StateFlow<ProProfileContract.State>
    fun onStartShiftClicked()
    fun onLogoutClicked()
    fun onLogoutConfirmed()
    fun onLogoutDismissed()

    companion object {
        fun create(
            componentContext: ComponentContext,
            dependencies: ProProfileDependencies,
            showSnackBar: (SnackBarData) -> Unit,
            navigateAuth: () -> Unit
        ): ProProfileComponent {
            return DefaultProProfileComponent(
                componentContext = componentContext,
                dependencies = dependencies,
                showSnackBar = showSnackBar,
                navigateAuth = navigateAuth
            )
        }
    }
}
