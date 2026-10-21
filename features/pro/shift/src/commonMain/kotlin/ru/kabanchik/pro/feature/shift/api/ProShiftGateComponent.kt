package ru.kabanchik.pro.feature.shift.api

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.StateFlow
import ru.kabanchik.common.snackBar.api.SnackBarData
import ru.kabanchik.pro.feature.shift.internal.DefaultProShiftGateComponent

/**
 * Экран для оператора вне смены: работа с чатами недоступна, пока он не выйдет на смену.
 */
interface ProShiftGateComponent {
    val state: StateFlow<ProShiftGateContract.State>
    fun onStartShiftClicked()
    fun onRetryClicked()
    fun onLogoutClicked()
    fun onLogoutConfirmed()
    fun onLogoutDismissed()

    companion object {
        fun create(
            componentContext: ComponentContext,
            dependencies: ProShiftGateDependencies,
            showSnackBar: (SnackBarData) -> Unit,
            navigateHome: () -> Unit,
            navigateAuth: () -> Unit
        ): ProShiftGateComponent {
            return DefaultProShiftGateComponent(
                componentContext = componentContext,
                dependencies = dependencies,
                showSnackBar = showSnackBar,
                navigateHome = navigateHome,
                navigateAuth = navigateAuth
            )
        }
    }
}
