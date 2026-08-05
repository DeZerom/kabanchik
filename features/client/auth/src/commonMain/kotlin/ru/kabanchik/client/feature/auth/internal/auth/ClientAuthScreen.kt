package ru.kabanchik.client.feature.auth.internal.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import ru.kabanchik.client.feature.auth.api.auth.AuthComponent
import ru.kabanchik.common.feature.auth.api.CommonAuthContent
import ru.kabanchik.common.scaffold.toolbar.CommonToolbarModel
import ru.kabanchik.common.screenSize.PartFillingScreen
import ru.kabanchik.common.tools.textResource.TextResource
import ru.kabanchik.common.uiKit.widgets.toolbar.AffectScaffold

@Composable
internal fun ClientAuthScreen(component: AuthComponent) {
    val state by component.state.collectAsState()

    AffectScaffold(
        toolbar = CommonToolbarModel.Title(TextResource.Raw(""))
    )

    PartFillingScreen {
        CommonAuthContent(
            onLoginChange = component::onLoginChanged,
            onPasswordChange = component::onPasswordChanged,
            onAuthorizeClicked = component::onAuthorizeClicked,
            onCreateAccountClicked = component::onCreateAccountClicked,
            login = state.login,
            password = state.password,
            isLoading = state.isLoading,
            isAuthorizing = true,
            isPasswordVisible = state.isPasswordVisible,
            onChangePasswordVisibility = component::onChangePasswordVisibility
        )
    }
}
