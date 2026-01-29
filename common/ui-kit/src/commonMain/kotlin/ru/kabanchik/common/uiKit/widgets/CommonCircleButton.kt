package ru.kabanchik.common.uiKit.widgets

import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.kabanchik.common.uiKit.theme.KabanchikTheme

@Composable
fun CommonCircleButton(
    onClick: () -> Unit,
    painter: Painter,
    modifier: Modifier = Modifier,
    color: Color = KabanchikTheme.colors.interactive,
    iconColor: Color = KabanchikTheme.colors.mainText,
    iconSize: Dp = 32.dp
) {
    Surface(
        shape = CircleShape,
        color = color,
        onClick = onClick,
        modifier = modifier.sizeIn(
            minWidth = 48.dp,
            minHeight = 48.dp
        )
    ) {
        Icon(
            painter = painter,
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier.requiredSize(iconSize)
        )
    }
}