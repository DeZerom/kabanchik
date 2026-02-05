package ru.kabanchik.common.screenSize

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PartFillingScreen(
    content: @Composable () -> Unit
) {
    val windowSize = LocalWindowSizeCompositionProvider.current

    when (windowSize.widthSizeClass) {
        WindowWidthSizeClass.Compact -> {
            content()
        }
        else -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .widthIn(max = 600.dp)
                        .fillMaxSize()
                ) {
                    content()
                }
            }
        }
    }
}