package ru.kabanchik.common.uiKit.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.kabanchik.common.uiKit.VSpacer
import ru.kabanchik.common.uiKit.icons.KabanchikIcons
import ru.kabanchik.common.uiKit.theme.KabanchikTheme
import ru.kabanchik.common.uiKit.theme.extraSmallText

class CommonBottomBarItem(
    val title: String,
    val icon: Painter,
)

@Composable
fun CommonBottomBar(
    items: List<CommonBottomBarItem>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(KabanchikTheme.colors.background)
            .windowInsetsPadding(WindowInsets.navigationBars)
    ) {
        HorizontalDivider(color = KabanchikTheme.colors.card)
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            items.forEachIndexed { index, item ->
                CommonBottomBarTab(
                    item = item,
                    isSelected = index == selectedIndex,
                    onClick = { onSelect(index) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun CommonBottomBarTab(
    item: CommonBottomBarItem,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val color = if (isSelected) {
        KabanchikTheme.colors.accent
    } else {
        KabanchikTheme.colors.secondaryText
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .selectable(
                selected = isSelected,
                onClick = onClick,
                role = Role.Tab
            )
            .padding(vertical = 8.dp)
    ) {
        Icon(
            painter = item.icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(BottomBarIconSize)
        )
        VSpacer(4.dp)
        Text(
            text = item.title,
            style = KabanchikTheme.typography.extraSmallText,
            fontWeight = FontWeight.Medium,
            color = color
        )
    }
}

private val BottomBarIconSize = 20.dp

@Preview
@Composable
private fun CommonBottomBarPreview() {
    KabanchikTheme {
        Scaffold(
            bottomBar = {
                CommonBottomBar(
                    items = listOf(
                        CommonBottomBarItem(title = "Чаты", icon = KabanchikIcons.Chat20),
                        CommonBottomBarItem(title = "Профиль", icon = KabanchikIcons.Profile80),
                    ),
                    selectedIndex = 1,
                    onSelect = {}
                )
            }
        ) {}
    }
}
