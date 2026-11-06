package ru.kabanchik.pro.feature.shift.api

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kabanchik.features.pro.shift.generated.resources.Res
import kabanchik.features.pro.shift.generated.resources.shift_gate_description
import kabanchik.features.pro.shift.generated.resources.shift_gate_error_description
import kabanchik.features.pro.shift.generated.resources.shift_gate_error_title
import kabanchik.features.pro.shift.generated.resources.shift_gate_logout
import kabanchik.features.pro.shift.generated.resources.shift_gate_retry
import kabanchik.features.pro.shift.generated.resources.shift_gate_start
import kabanchik.features.pro.shift.generated.resources.shift_gate_title
import kabanchik.features.pro.shift.generated.resources.shift_gate_toolbar_title
import org.jetbrains.compose.resources.stringResource
import ru.kabanchik.common.feature.profile.api.CommonLogoutDialog
import ru.kabanchik.common.scaffold.toolbar.CommonToolbarModel
import ru.kabanchik.common.screenSize.PartFillingScreen
import ru.kabanchik.common.tools.extensions.asTextResource
import ru.kabanchik.common.uiKit.VSpacer
import ru.kabanchik.common.uiKit.theme.KabanchikTheme
import ru.kabanchik.common.uiKit.theme.bodyMedium
import ru.kabanchik.common.uiKit.theme.headlineSmall
import ru.kabanchik.common.uiKit.widgets.CommonButton
import ru.kabanchik.common.uiKit.widgets.CommonScreenLoader
import ru.kabanchik.common.uiKit.widgets.toolbar.AffectScaffold

@Composable
fun ProShiftGateScreen(component: ProShiftGateComponent) {
    val state by component.state.collectAsState()

    AffectScaffold(
        toolbar = CommonToolbarModel.Title(Res.string.shift_gate_toolbar_title.asTextResource())
    )

    PartFillingScreen {
        if (state.isLoading) {
            CommonScreenLoader()
        } else {
            ProShiftGateContent(
                isError = state.isError,
                isStartInProgress = state.isStartInProgress,
                onStartShiftClick = component::onStartShiftClicked,
                onRetryClick = component::onRetryClicked,
                onLogoutClick = component::onLogoutClicked
            )
        }
    }

    if (state.isLogoutDialogVisible) {
        CommonLogoutDialog(
            isLogoutInProgress = state.isLogoutInProgress,
            onConfirm = component::onLogoutConfirmed,
            onDismiss = component::onLogoutDismissed
        )
    }
}

@Composable
private fun ProShiftGateContent(
    isError: Boolean,
    isStartInProgress: Boolean,
    onStartShiftClick: () -> Unit,
    onRetryClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 24.dp)
    ) {
        Text(
            text = stringResource(if (isError) Res.string.shift_gate_error_title else Res.string.shift_gate_title),
            style = KabanchikTheme.typography.headlineSmall,
            color = KabanchikTheme.colors.mainText,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = stringResource(
                if (isError) Res.string.shift_gate_error_description else Res.string.shift_gate_description
            ),
            style = KabanchikTheme.typography.bodyMedium,
            color = KabanchikTheme.colors.secondaryText,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        VSpacer(8.dp)
        if (isError) {
            CommonButton(
                text = stringResource(Res.string.shift_gate_retry),
                onClick = onRetryClick,
                modifier = Modifier.fillMaxWidth()
            )
        } else {
            CommonButton(
                text = stringResource(Res.string.shift_gate_start),
                onClick = onStartShiftClick,
                isLoading = isStartInProgress,
                modifier = Modifier.fillMaxWidth()
            )
        }
        CommonButton(
            text = stringResource(Res.string.shift_gate_logout),
            onClick = onLogoutClick,
            isEnabled = !isStartInProgress,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview
@Composable
private fun ProShiftGateContentPreview() {
    KabanchikTheme {
        Scaffold {
            ProShiftGateContent(
                isError = false,
                isStartInProgress = false,
                onStartShiftClick = {},
                onRetryClick = {},
                onLogoutClick = {}
            )
        }
    }
}
