package ru.kabanchik.client.feature.auth.internal.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import kabanchik.features.client.auth.generated.resources.Res
import kabanchik.features.client.auth.generated.resources.auth_reg_create_account
import kabanchik.features.client.auth.generated.resources.auth_reg_has_acc
import kabanchik.features.client.auth.generated.resources.auth_reg_login
import kabanchik.features.client.auth.generated.resources.auth_reg_password
import kabanchik.features.client.auth.generated.resources.auth_reg_registration
import org.jetbrains.compose.resources.stringResource
import ru.kabanchik.client.feature.auth.api.register.RegisterComponent
import ru.kabanchik.client.feature.auth.api.register.RegisterContract
import ru.kabanchik.common.uiKit.KabanchikImages
import ru.kabanchik.common.uiKit.VSpacer
import ru.kabanchik.common.uiKit.theme.KabanchikTheme
import ru.kabanchik.common.uiKit.theme.bigTitle
import ru.kabanchik.common.uiKit.widgets.CommonButton
import ru.kabanchik.common.uiKit.widgets.CommonTextInput

@Composable
internal fun RegisterScreen(component: RegisterComponent) {
    val state by component.state.collectAsState()

    Scaffold {
        Content(
            state = state,
            onLoginChange = component::onLoginChanged,
            onPasswordChange = component::onPasswordChanged,
            onCreateAccount = component::onCreateAccountClicked,
            onHaveAccount = component::onHaveAccountClicked
        )
    }
}

@Composable
private fun Content(
    state: RegisterContract.State,
    onLoginChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onCreateAccount: () -> Unit,
    onHaveAccount: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Image(
            painter = KabanchikImages.AppImage,
            contentDescription = null,
            modifier = Modifier.size(186.dp)
        )
        Text(
            text = stringResource(Res.string.auth_reg_registration),
            style = KabanchikTheme.typography.bigTitle,
            color = KabanchikTheme.colors.mainText
        )
        VSpacer(24.dp)
        CommonTextInput(
            value = state.login,
            label = stringResource(Res.string.auth_reg_login),
            onValueChange = onLoginChange,
            modifier = Modifier.fillMaxWidth()
        )
        VSpacer(16.dp)
        CommonTextInput(
            value = state.password,
            label = stringResource(Res.string.auth_reg_password),
            onValueChange = onPasswordChange,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        VSpacer(24.dp)
        CommonButton(
            onClick = onCreateAccount,
            text = stringResource(Res.string.auth_reg_create_account),
            backgroundColor = KabanchikTheme.colors.accent,
            textColor = KabanchikTheme.colors.mainText,
            modifier = Modifier.fillMaxWidth()
        )
        VSpacer(8.dp)
        CommonButton(
            onClick = onHaveAccount,
            text = stringResource(Res.string.auth_reg_has_acc),
            backgroundColor = KabanchikTheme.colors.interactive,
            textColor = KabanchikTheme.colors.mainTextInverted,
            modifier = Modifier.fillMaxWidth()
        )
    }
}