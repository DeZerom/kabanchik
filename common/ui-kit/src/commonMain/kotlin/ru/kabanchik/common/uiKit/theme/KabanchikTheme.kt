package ru.kabanchik.common.uiKit.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

@Composable
fun KabanchikTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            background = KabanchikColors.background,
            primary = KabanchikColors.accent,
            onPrimary = KabanchikColors.interactive
        ),
        typography = KabanchikTypography.typography,
        shapes = KabanchikShape.shape,
        content = content
    )
}

object KabanchikTheme {
    val colors @Composable get() = KabanchikColors
    val typography = KabanchikTypography
    val shapes = KabanchikShape
}