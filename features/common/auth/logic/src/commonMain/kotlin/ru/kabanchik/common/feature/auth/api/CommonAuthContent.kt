package ru.kabanchik.common.feature.auth.api

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kabanchik.features.common.auth.logic.generated.resources.Res
import kabanchik.features.common.auth.logic.generated.resources.auth_auth_create_account
import kabanchik.features.common.auth.logic.generated.resources.auth_auth_enter
import kabanchik.features.common.auth.logic.generated.resources.auth_auth_entrance
import kabanchik.features.common.auth.logic.generated.resources.auth_auth_login
import kabanchik.features.common.auth.logic.generated.resources.auth_auth_password
import org.jetbrains.compose.resources.stringResource
import ru.kabanchik.common.modifier.keyboardInsetsPadding
import ru.kabanchik.common.uiKit.KabanchikImages
import ru.kabanchik.common.uiKit.VSpacer
import ru.kabanchik.common.uiKit.theme.KabanchikTheme
import ru.kabanchik.common.uiKit.theme.bigTitle
import ru.kabanchik.common.uiKit.widgets.CommonButton
import ru.kabanchik.common.uiKit.widgets.CommonTextInput

@Composable
fun CommonAuthContent(
    login: String,
    password: String,
    isLoading: Boolean,
    hasRegisterButton: Boolean,
    onAuthorizeClicked: () -> Unit,
    onLoginChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    onCreateAccountClicked: () -> Unit = {},
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .keyboardInsetsPadding()
            .verticalScroll(rememberScrollState())
    ) {
        Image(
            painter = KabanchikImages.AppImage,
            contentDescription = null,
            modifier = Modifier.size(186.dp)
        )
        Text(
            text = stringResource(Res.string.auth_auth_entrance),
            style = KabanchikTheme.typography.bigTitle,
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
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
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
        if (hasRegisterButton) {
            VSpacer(8.dp)
            CommonButton(
                onClick = onCreateAccountClicked,
                text = stringResource(Res.string.auth_auth_create_account),
                backgroundColor = KabanchikTheme.colors.interactive,
                textColor = KabanchikTheme.colors.mainTextInverted,
                isEnabled = !isLoading,
                modifier = Modifier.fillMaxWidth()
            )
        }
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
                hasRegisterButton = true,
                onAuthorizeClicked = {},
                onLoginChange = {},
                onPasswordChange = {}
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
                hasRegisterButton = false,
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
                hasRegisterButton = true,
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
                hasRegisterButton = true,
                onAuthorizeClicked = {},
                onLoginChange = {},
                onPasswordChange = {}
            )
        }
    }
}
