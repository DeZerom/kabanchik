package ru.kabanchik.pro.feature.chat.internal.end

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import kabanchik.features.pro.chat.generated.resources.Res
import kabanchik.features.pro.chat.generated.resources.chat_end_confirm
import kabanchik.features.pro.chat.generated.resources.chat_end_status
import kabanchik.features.pro.chat.generated.resources.chat_end_title
import org.jetbrains.compose.resources.stringResource
import ru.kabanchik.common.features.chat.logic.details.toTextResource
import ru.kabanchik.common.tools.extensions.getValue
import ru.kabanchik.common.uiKit.VSpacer
import ru.kabanchik.common.uiKit.theme.KabanchikTheme
import ru.kabanchik.common.uiKit.theme.bodyMedium
import ru.kabanchik.common.uiKit.widgets.CommonBottomSheet
import ru.kabanchik.common.uiKit.widgets.CommonBottomSheetHeader
import ru.kabanchik.common.uiKit.widgets.CommonButton
import ru.kabanchik.common.uiKit.widgets.CommonRadioOption
import ru.kabanchik.pro.feature.chat.api.end.ProEndChatComponent

@Composable
internal fun ProEndChatSheet(component: ProEndChatComponent) {
    val state by component.state.collectAsState()

    CommonBottomSheet(
        onDismissRequest = component::onDismissRequested,
        isDismissible = !state.isEnding,
        header = {
            CommonBottomSheetHeader(
                title = stringResource(Res.string.chat_end_title),
                onCloseClick = component::onDismissRequested,
                isCloseEnabled = !state.isEnding,
            )
        }
    ) {
        VSpacer(24.dp)
        RequiredLabel(text = stringResource(Res.string.chat_end_status))
        VSpacer(8.dp)
        Column {
            state.resolutions.forEach { resolution ->
                CommonRadioOption(
                    text = resolution.toTextResource().getValue(),
                    isSelected = resolution == state.selectedResolution,
                    isEnabled = !state.isEnding,
                    onClick = { component.onResolutionSelected(resolution) },
                )
            }
        }
        VSpacer(16.dp)
        CommonButton(
            text = stringResource(Res.string.chat_end_confirm),
            isLoading = state.isEnding,
            onClick = component::onEndClicked,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun RequiredLabel(text: String) {
    Text(
        text = buildAnnotatedString {
            append(text)
            withStyle(SpanStyle(color = KabanchikTheme.colors.error)) {
                append(" *")
            }
        },
        style = KabanchikTheme.typography.bodyMedium,
        color = KabanchikTheme.colors.secondaryText,
    )
}
