package ru.kabanchik.common.feature.profile.api

import androidx.compose.runtime.Composable
import kabanchik.features.common.profile.generated.resources.Res
import kabanchik.features.common.profile.generated.resources.profile_logout_dialog_confirm
import kabanchik.features.common.profile.generated.resources.profile_logout_dialog_dismiss
import kabanchik.features.common.profile.generated.resources.profile_logout_dialog_text
import kabanchik.features.common.profile.generated.resources.profile_logout_dialog_title
import org.jetbrains.compose.resources.stringResource
import ru.kabanchik.common.uiKit.widgets.CommonConfirmDialog

/**
 * Диалог подтверждения выхода из аккаунта.
 */
@Composable
fun CommonLogoutDialog(
    isLogoutInProgress: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    text: String = stringResource(Res.string.profile_logout_dialog_text),
) {
    CommonConfirmDialog(
        title = stringResource(Res.string.profile_logout_dialog_title),
        text = text,
        confirmText = stringResource(Res.string.profile_logout_dialog_confirm),
        dismissText = stringResource(Res.string.profile_logout_dialog_dismiss),
        onConfirm = onConfirm,
        onDismiss = onDismiss,
        isConfirmLoading = isLogoutInProgress
    )
}
