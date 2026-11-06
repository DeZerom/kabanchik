package ru.kabanchik.pro.feature.profile.api

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import kabanchik.features.pro.profile.generated.resources.Res
import kabanchik.features.pro.profile.generated.resources.profile_end_shift
import kabanchik.features.pro.profile.generated.resources.profile_end_shift_dialog_confirm
import kabanchik.features.pro.profile.generated.resources.profile_end_shift_dialog_dismiss
import kabanchik.features.pro.profile.generated.resources.profile_end_shift_dialog_text
import kabanchik.features.pro.profile.generated.resources.profile_end_shift_dialog_title
import kabanchik.features.pro.profile.generated.resources.profile_logout_on_shift_dialog_text
import kabanchik.features.pro.profile.generated.resources.profile_title
import org.jetbrains.compose.resources.stringResource
import ru.kabanchik.common.feature.profile.api.CommonProfileContent
import ru.kabanchik.common.scaffold.toolbar.CommonToolbarModel
import ru.kabanchik.common.screenSize.PartFillingScreen
import ru.kabanchik.common.tools.textResource.TextResource
import ru.kabanchik.common.uiKit.widgets.CommonButton
import ru.kabanchik.common.uiKit.widgets.CommonConfirmDialog
import ru.kabanchik.common.uiKit.widgets.toolbar.AffectScaffold

@Composable
fun ProProfileScreen(component: ProProfileComponent) {
    val state by component.state.collectAsState()

    AffectScaffold(
        toolbar = CommonToolbarModel.Title(
            title = TextResource.Id(Res.string.profile_title)
        )
    )

    PartFillingScreen {
        CommonProfileContent(
            login = state.login,
            isLogoutDialogVisible = state.isLogoutDialogVisible,
            isLogoutInProgress = state.isLogoutInProgress,
            onLogoutClick = component::onLogoutClicked,
            onLogoutConfirm = component::onLogoutConfirmed,
            onLogoutDismiss = component::onLogoutDismissed,
            // Профиль доступен только на смене, и выход из аккаунта ее завершает
            logoutDialogText = stringResource(Res.string.profile_logout_on_shift_dialog_text),
            additionalActions = {
                CommonButton(
                    text = stringResource(Res.string.profile_end_shift),
                    onClick = component::onEndShiftClicked,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        )
    }

    if (state.isEndShiftDialogVisible) {
        CommonConfirmDialog(
            title = stringResource(Res.string.profile_end_shift_dialog_title),
            text = stringResource(Res.string.profile_end_shift_dialog_text),
            confirmText = stringResource(Res.string.profile_end_shift_dialog_confirm),
            dismissText = stringResource(Res.string.profile_end_shift_dialog_dismiss),
            onConfirm = component::onEndShiftConfirmed,
            onDismiss = component::onEndShiftDismissed,
            isConfirmLoading = state.isEndShiftInProgress
        )
    }
}
