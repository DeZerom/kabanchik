package ru.kabanchik.client.feature.profile.api

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import kabanchik.features.client.profile.generated.resources.Res
import kabanchik.features.client.profile.generated.resources.profile_title
import ru.kabanchik.common.feature.profile.api.CommonProfileContent
import ru.kabanchik.common.scaffold.toolbar.CommonToolbarModel
import ru.kabanchik.common.screenSize.PartFillingScreen
import ru.kabanchik.common.tools.textResource.TextResource
import ru.kabanchik.common.uiKit.widgets.toolbar.AffectScaffold

@Composable
fun ClientProfileScreen(component: ClientProfileComponent) {
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
            onLogoutDismiss = component::onLogoutDismissed
        )
    }
}
