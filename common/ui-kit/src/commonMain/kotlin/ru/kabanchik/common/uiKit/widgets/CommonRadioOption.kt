package ru.kabanchik.common.uiKit.widgets

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.kabanchik.common.uiKit.HSpacer
import ru.kabanchik.common.uiKit.theme.KabanchikTheme
import ru.kabanchik.common.uiKit.theme.bodyMedium

/**
 * Строка списка с радиокнопкой. Нажимается целиком.
 */
@Composable
fun CommonRadioOption(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 48.dp)
            .selectable(
                selected = isSelected,
                enabled = isEnabled,
                role = Role.RadioButton,
                onClick = onClick
            )
    ) {
        RadioButton(
            selected = isSelected,
            onClick = null,
            enabled = isEnabled,
            colors = RadioButtonDefaults.colors(
                selectedColor = KabanchikTheme.colors.accent,
                unselectedColor = KabanchikTheme.colors.secondaryText,
            )
        )
        HSpacer(4.dp)
        Text(
            text = text,
            style = KabanchikTheme.typography.bodyMedium,
            color = KabanchikTheme.colors.mainText,
        )
    }
}

@Preview
@Composable
private fun CommonRadioOptionPreview() {
    KabanchikTheme {
        Surface(color = KabanchikTheme.colors.background) {
            CommonRadioOption(
                text = "Закрыто",
                isSelected = true,
                onClick = {}
            )
        }
    }
}
