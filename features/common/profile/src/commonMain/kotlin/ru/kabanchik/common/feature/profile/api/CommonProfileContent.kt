package ru.kabanchik.common.feature.profile.api

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kabanchik.features.common.profile.generated.resources.Res
import kabanchik.features.common.profile.generated.resources.profile_logout
import kabanchik.features.common.profile.generated.resources.profile_logout_dialog_confirm
import kabanchik.features.common.profile.generated.resources.profile_logout_dialog_dismiss
import kabanchik.features.common.profile.generated.resources.profile_logout_dialog_text
import kabanchik.features.common.profile.generated.resources.profile_logout_dialog_title
import org.jetbrains.compose.resources.stringResource
import ru.kabanchik.common.uiKit.VSpacer
import ru.kabanchik.common.uiKit.icons.KabanchikIcons
import ru.kabanchik.common.uiKit.theme.KabanchikTheme
import ru.kabanchik.common.uiKit.theme.headlineSmall
import ru.kabanchik.common.uiKit.widgets.CommonButton
import ru.kabanchik.common.uiKit.widgets.CommonConfirmDialog

/**
 * Общий экран профиля: аватар, логин, кнопка выхода и диалог подтверждения выхода.
 * Кнопки, специфичные для приложения, передаются через [additionalActions] и рисуются над кнопкой выхода.
 */
@Composable
fun CommonProfileContent(
    login: String,
    isLogoutDialogVisible: Boolean,
    isLogoutInProgress: Boolean,
    onLogoutClick: () -> Unit,
    onLogoutConfirm: () -> Unit,
    onLogoutDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    additionalActions: @Composable ColumnScope.() -> Unit = {},
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 24.dp)
    ) {
        Icon(
            painter = KabanchikIcons.Profile80,
            contentDescription = null,
            tint = KabanchikTheme.colors.mainText,
            modifier = Modifier.size(AvatarSize)
        )
        Text(
            text = login,
            style = KabanchikTheme.typography.headlineSmall,
            color = KabanchikTheme.colors.mainText,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        VSpacer(8.dp)
        additionalActions()
        CommonButton(
            text = stringResource(Res.string.profile_logout),
            onClick = onLogoutClick,
            modifier = Modifier.fillMaxWidth()
        )
    }

    if (isLogoutDialogVisible) {
        CommonConfirmDialog(
            title = stringResource(Res.string.profile_logout_dialog_title),
            text = stringResource(Res.string.profile_logout_dialog_text),
            confirmText = stringResource(Res.string.profile_logout_dialog_confirm),
            dismissText = stringResource(Res.string.profile_logout_dialog_dismiss),
            onConfirm = onLogoutConfirm,
            onDismiss = onLogoutDismiss,
            isConfirmLoading = isLogoutInProgress
        )
    }
}

private val AvatarSize = 80.dp

@Preview
@Composable
private fun CommonProfileContentPreview() {
    KabanchikTheme {
        Scaffold {
            CommonProfileContent(
                login = "Love Kumar",
                isLogoutDialogVisible = false,
                isLogoutInProgress = false,
                onLogoutClick = {},
                onLogoutConfirm = {},
                onLogoutDismiss = {},
                additionalActions = {
                    CommonButton(
                        text = "Начать смену",
                        onClick = {},
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            )
        }
    }
}
