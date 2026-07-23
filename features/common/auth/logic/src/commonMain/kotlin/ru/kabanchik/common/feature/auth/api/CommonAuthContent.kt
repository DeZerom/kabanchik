package ru.kabanchik.common.feature.auth.api

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kabanchik.features.common.auth.logic.generated.resources.Res
import kabanchik.features.common.auth.logic.generated.resources.auth_auth_create_account
import kabanchik.features.common.auth.logic.generated.resources.auth_auth_enter
import kabanchik.features.common.auth.logic.generated.resources.auth_auth_entrance
import kabanchik.features.common.auth.logic.generated.resources.auth_auth_login
import kabanchik.features.common.auth.logic.generated.resources.auth_auth_no_acc
import kabanchik.features.common.auth.logic.generated.resources.auth_auth_password
import kabanchik.features.common.auth.logic.generated.resources.auth_reg_has_acc
import kabanchik.features.common.auth.logic.generated.resources.auth_reg_registration
import org.jetbrains.compose.resources.stringResource
import ru.kabanchik.common.modifier.keyboardInsetsPadding
import ru.kabanchik.common.uiKit.HSpacer
import ru.kabanchik.common.uiKit.KabanchikImages
import ru.kabanchik.common.uiKit.VSpacer
import ru.kabanchik.common.uiKit.icons.KabanchikIcons
import ru.kabanchik.common.uiKit.icons.extensions.Eye16
import ru.kabanchik.common.uiKit.icons.extensions.EyeOff16
import ru.kabanchik.common.uiKit.theme.KabanchikTheme
import ru.kabanchik.common.uiKit.theme.bodyS
import ru.kabanchik.common.uiKit.theme.headlineLarge
import ru.kabanchik.common.uiKit.widgets.CommonButton
import ru.kabanchik.common.uiKit.widgets.CommonTextInput

@Composable
fun CommonAuthContent(
    login: String,
    password: String,
    isLoading: Boolean,
    isAuthorizing: Boolean,
    isPasswordVisible: Boolean,
    onAuthorizeClicked: () -> Unit,
    onLoginChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    onCreateAccountClicked: () -> Unit = {},
    onHaveAccountClicked: () -> Unit = {},
    onChangePasswordVisibility: () -> Unit = {},
) {
    Column(
        verticalArrangement = Arrangement.Top,
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .keyboardInsetsPadding()
            .verticalScroll(rememberScrollState())
    ) {
        Image(
            painter = KabanchikImages.AppImage,
            contentDescription = null,
            modifier = Modifier
                .size(160.dp)
                .align(Alignment.CenterHorizontally)
        )
        Text(
            text = if (isAuthorizing) stringResource(Res.string.auth_auth_entrance) else stringResource(Res.string.auth_reg_registration),
            style = KabanchikTheme.typography.headlineLarge,
            color = KabanchikTheme.colors.mainText
        )
        VSpacer(24.dp)
        CommonTextInput(
            value = login,
            label = stringResource(Res.string.auth_auth_login),
            onValueChange = onLoginChange,
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        VSpacer(16.dp)
        CommonTextInput(
            value = password,
            label = stringResource(Res.string.auth_auth_password),
            onValueChange = onPasswordChange,
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            singleLine = true,
            trailingIcon = {
                IconButton(
                    onClick = onChangePasswordVisibility,
                ) {
                    Icon(
                        imageVector = if (isPasswordVisible) KabanchikIcons.EyeOff16 else KabanchikIcons.Eye16,
                        contentDescription = null,
                        tint = KabanchikTheme.colors.secondaryText
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
        VSpacer(24.dp)
        CommonButton(
            onClick = onAuthorizeClicked,
            text = stringResource(Res.string.auth_auth_enter),
            backgroundColor = KabanchikTheme.colors.accent,
            textColor = KabanchikTheme.colors.mainText,
            isLoading = isLoading,
            modifier = Modifier.fillMaxWidth()
        )
        VSpacer(16.dp)
        Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text(
                text = stringResource(
                    if (isAuthorizing) Res.string.auth_auth_no_acc else Res.string.auth_reg_has_acc
                ),
                style = KabanchikTheme.typography.bodyS,
                color = KabanchikTheme.colors.secondaryText
            )
            HSpacer(4.dp)
            Text(
                text = stringResource(
                    if (isAuthorizing) Res.string.auth_auth_create_account else Res.string.auth_auth_enter
                ),
                style = KabanchikTheme.typography.bodyS,
                color = KabanchikTheme.colors.accent,
                modifier = Modifier.clickable {
                    if (!isLoading) {
                        if (isAuthorizing) onCreateAccountClicked() else onHaveAccountClicked()
                    }
                }
            )
        }
        VSpacer(16.dp)
    }
}

@Composable
@Preview
private fun AuthPreview() {
    KabanchikTheme {
        Scaffold {
            CommonAuthContent(
                login = "",
                password = "",
                isLoading = false,
                isAuthorizing = true,
                isPasswordVisible = false,
                onAuthorizeClicked = {},
                onLoginChange = {},
                onPasswordChange = {},
            )
        }
    }
}

@Composable
@Preview
private fun AuthOneButtonPreview() {
    KabanchikTheme {
        Scaffold {
            CommonAuthContent(
                login = "",
                password = "",
                isLoading = false,
                isAuthorizing = false,
                isPasswordVisible = true,
                onAuthorizeClicked = {},
                onLoginChange = {},
                onPasswordChange = {}
            )
        }
    }
}

@Composable
@Preview
private fun AuthFullPreview() {
    KabanchikTheme {
        Scaffold {
            CommonAuthContent(
                login = "qwe",
                password = "qwe",
                isLoading = false,
                isAuthorizing = true,
                isPasswordVisible = false,
                onAuthorizeClicked = {},
                onLoginChange = {},
                onPasswordChange = {}
            )
        }
    }
}

@Composable
@Preview
private fun AuthLoadingPreview() {
    KabanchikTheme {
        Scaffold {
            CommonAuthContent(
                login = "qwe",
                password = "qwe",
                isLoading = true,
                isAuthorizing = true,
                isPasswordVisible = true,
                onAuthorizeClicked = {},
                onLoginChange = {},
                onPasswordChange = {}
            )
        }
    }
}
