package ru.kabanchik.common.modifier

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun Modifier.keyboardInsetsPadding(): Modifier {
    return windowInsetsPadding(WindowInsets.navigationBars.union(WindowInsets.ime))
}
