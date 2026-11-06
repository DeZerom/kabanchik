package ru.kabanchik.common.features.chat.logic.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kabanchik.features.common.chat.logic.generated.resources.Res
import kabanchik.features.common.chat.logic.generated.resources.chat_details_resolution_title
import org.jetbrains.compose.resources.stringResource
import ru.kabanchik.common.chat.model.CommonChatResolution
import ru.kabanchik.common.feature.chat.model.CommonUiMessage
import ru.kabanchik.common.tools.extensions.getValue
import ru.kabanchik.common.uiKit.HSpacer
import ru.kabanchik.common.uiKit.VSpacer
import ru.kabanchik.common.uiKit.theme.KabanchikTheme
import ru.kabanchik.common.uiKit.theme.bodyS
import ru.kabanchik.common.uiKit.theme.cardDefault
import ru.kabanchik.common.uiKit.theme.smallTitle

@Composable
internal fun CommonChatResolutionCard(
    resolution: CommonUiMessage.Resolution,
    modifier: Modifier = Modifier
) {
    Surface(
        color = KabanchikTheme.colors.card,
        shape = KabanchikTheme.shapes.cardDefault,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(Res.string.chat_details_resolution_title),
                    style = KabanchikTheme.typography.smallTitle,
                    color = KabanchikTheme.colors.mainText,
                    modifier = Modifier.weight(1f)
                )
                HSpacer(8.dp)
                Text(
                    text = resolution.status.getValue(),
                    style = KabanchikTheme.typography.bodyS,
                    color = KabanchikTheme.colors.accent,
                )
            }
            VSpacer(8.dp)
            Text(
                text = resolution.time,
                style = KabanchikTheme.typography.bodyS,
                color = KabanchikTheme.colors.secondaryText,
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}

@Preview
@Composable
private fun CommonChatResolutionCardPreview() {
    KabanchikTheme {
        Surface(color = KabanchikTheme.colors.background) {
            CommonChatResolutionCard(
                resolution = CommonUiMessage.Resolution(
                    id = "resolution",
                    status = CommonChatResolution.Closed.toTextResource(),
                    time = "18:45",
                ),
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}
