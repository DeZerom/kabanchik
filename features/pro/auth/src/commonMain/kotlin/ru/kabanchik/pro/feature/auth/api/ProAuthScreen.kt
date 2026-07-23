package ru.kabanchik.pro.feature.auth.api

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import ru.kabanchik.common.feature.auth.api.CommonAuthContent
import ru.kabanchik.common.screenSize.PartFillingScreen
import ru.kabanchik.common.uiKit.widgets.toolbar.AffectScaffold

@Composable
fun ProAuthScreen(component: ProAuthComponent) {
    val state by component.uiState.collectAsState()

    AffectScaffold()

    PartFillingScreen {
        CommonAuthContent(
            login = state.login,
            password = state.password,
            isLoading = state.isLoading,
            isAuthorizing = true,
            isRegistrationAvailable = false,
            onAuthorizeClicked = component::onAuthorizeClicked,
            onLoginChange = component::onLoginChanged,
            onPasswordChange = component::onPasswordChanged,
            isPasswordVisible = state.isPasswordVisible,
            onChangePasswordVisibility = component::onChangePasswordVisibility,
        )
    }
}
