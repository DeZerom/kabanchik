package ru.kabanchik.common.uiKit.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.kabanchik.common.uiKit.theme.KabanchikTheme
import ru.kabanchik.common.uiKit.theme.bodyMedium

/**
 * Переключатель вкладок одинаковой ширины.
 */
@Composable
fun CommonSegmentedTabs(
    tabs: List<String>,
    selectedIndex: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val containerShape = RoundedCornerShape(12.dp)
    val tabShape = RoundedCornerShape(8.dp)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(containerShape)
            .background(KabanchikTheme.colors.card)
            .padding(4.dp)
    ) {
        tabs.forEachIndexed { index, title ->
            val isSelected = index == selectedIndex
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .weight(1f)
                    .heightIn(min = 32.dp)
                    .clip(tabShape)
                    .background(if (isSelected) KabanchikTheme.colors.accent else Color.Transparent)
                    .selectable(
                        selected = isSelected,
                        role = Role.Tab,
                        onClick = { onTabSelected(index) }
                    )
            ) {
                Text(
                    text = title,
                    style = KabanchikTheme.typography.bodyMedium,
                    color = if (isSelected) KabanchikTheme.colors.mainText else KabanchikTheme.colors.secondaryText,
                )
            }
        }
    }
}

@Preview
@Composable
private fun CommonSegmentedTabsPreview() {
    KabanchikTheme {
        Surface(color = KabanchikTheme.colors.background) {
            CommonSegmentedTabs(
                tabs = listOf("Активные", "Завершённые"),
                selectedIndex = 1,
                onTabSelected = {}
            )
        }
    }
}
