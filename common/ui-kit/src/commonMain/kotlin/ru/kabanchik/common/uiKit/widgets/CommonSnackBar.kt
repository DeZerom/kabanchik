package ru.kabanchik.common.uiKit.widgets

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.kabanchik.common.snackBar.api.CommonVisuals
import ru.kabanchik.common.snackBar.api.CommonVisualsType
import ru.kabanchik.common.tools.extensions.getValue
import ru.kabanchik.common.uiKit.HSpacer
import ru.kabanchik.common.uiKit.KabanchikIcons
import ru.kabanchik.common.uiKit.theme.KabanchikTheme
import ru.kabanchik.common.uiKit.theme.cardDefault
import ru.kabanchik.common.uiKit.theme.smallText

@Composable
fun CommonSnackBar(
    commonVisuals: CommonVisuals,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = KabanchikTheme.shapes.cardDefault,
        color = KabanchikTheme.colors.cardInverted,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = 36.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(all = 16.dp)
        ) {
            Icon(
                painter = when (commonVisuals.type) {
                    CommonVisualsType.Error -> KabanchikIcons.Close24
                    CommonVisualsType.Success -> KabanchikIcons.Check24
                },
                contentDescription = null,
                modifier = Modifier.size(36.dp),
                tint = when (commonVisuals.type) {
                    CommonVisualsType.Error -> KabanchikTheme.colors.error
                    CommonVisualsType.Success -> KabanchikTheme.colors.success
                }
            )
            HSpacer(8.dp)
            Text(
                text = commonVisuals.textResource.getValue(),
                style = KabanchikTheme.typography.smallText,
                color = KabanchikTheme.colors.mainTextInverted
            )
        }
    }
}