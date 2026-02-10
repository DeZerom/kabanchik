package ru.kabanchik.pro.feature.auth.internal

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import kabanchik.features.pro.auth.generated.resources.Res
import kabanchik.features.pro.auth.generated.resources.pro_auth_auth_enter
import kabanchik.features.pro.auth.generated.resources.pro_auth_auth_entrance
import kabanchik.features.pro.auth.generated.resources.pro_auth_auth_login
import kabanchik.features.pro.auth.generated.resources.pro_auth_auth_password
import org.jetbrains.compose.resources.stringResource
import ru.kabanchik.common.uiKit.KabanchikImages
import ru.kabanchik.common.uiKit.VSpacer
import ru.kabanchik.common.uiKit.theme.KabanchikTheme
import ru.kabanchik.common.uiKit.theme.bigTitle
import ru.kabanchik.common.uiKit.widgets.CommonButton
import ru.kabanchik.common.uiKit.widgets.CommonTextInput
import ru.kabanchik.pro.feature.auth.api.ProAuthContract

@Composable
internal fun ProAuthContent(
    state: ProAuthContract.State,
    onAuthorizeClicked: () -> Unit,
    onLoginChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit
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
            text = stringResource(Res.string.pro_auth_auth_entrance),
            style = KabanchikTheme.typography.bigTitle,
            color = KabanchikTheme.colors.mainText
        )
        VSpacer(24.dp)
        CommonTextInput(
            value = state.login,
            label = stringResource(Res.string.pro_auth_auth_login),
            onValueChange = onLoginChanged,
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        VSpacer(16.dp)
        CommonTextInput(
            value = state.password,
            label = stringResource(Res.string.pro_auth_auth_password),
            onValueChange = onPasswordChanged,
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        VSpacer(24.dp)
        CommonButton(
            onClick = onAuthorizeClicked,
            text = stringResource(Res.string.pro_auth_auth_enter),
            backgroundColor = KabanchikTheme.colors.accent,
            textColor = KabanchikTheme.colors.mainText,
            isLoading = state.isLoading,
            modifier = Modifier.fillMaxWidth()
        )
    }
}