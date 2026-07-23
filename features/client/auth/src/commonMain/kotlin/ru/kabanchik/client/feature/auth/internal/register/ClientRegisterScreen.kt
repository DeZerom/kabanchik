package ru.kabanchik.client.feature.auth.internal.register

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import ru.kabanchik.client.feature.auth.api.register.ClientRegisterComponent
import ru.kabanchik.common.feature.auth.api.CommonAuthContent
import ru.kabanchik.common.scaffold.toolbar.CommonToolbarModel
import ru.kabanchik.common.screenSize.PartFillingScreen
import ru.kabanchik.common.tools.textResource.TextResource
import ru.kabanchik.common.uiKit.widgets.toolbar.AffectScaffold

@Composable
internal fun ClientRegisterScreen(component: ClientRegisterComponent) {
    val state by component.state.collectAsState()

    AffectScaffold(
        toolbar = CommonToolbarModel.BackButtonTitle(
            title = TextResource.Raw(""),
            onBackClicked = component::onHaveAccountClicked
        )
    )

    PartFillingScreen {
        CommonAuthContent(
            login = state.login,
            password = state.password,
            isLoading = state.isLoading,
            isAuthorizing = false,
            onAuthorizeClicked = component::onCreateAccountClicked,
            onLoginChange = component::onLoginChanged,
            onPasswordChange = component::onPasswordChanged,
            isPasswordVisible = state.isPasswordVisible,
            onHaveAccountClicked = component::onHaveAccountClicked,
            onChangePasswordVisibility = component::onChangePasswordVisibility,
        )
    }
}
