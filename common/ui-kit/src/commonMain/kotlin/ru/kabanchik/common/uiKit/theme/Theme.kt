package ru.kabanchik.common.uiKit.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

@Composable
fun KabanchikTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            background = KabanchikColors.Background,
            primary = KabanchikColors.Accent,
            onPrimary = KabanchikColors.Interactive
        ),
        typography = KabanchikTypography.typography,
        content = content
    )
}

object KabanchikTheme {
    val colors @Composable get() = KabanchikColors
    val typography = KabanchikTypography
}