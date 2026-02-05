package ru.kabanchik.common.screenSize

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.compositionLocalOf

val LocalWindowSizeCompositionProvider = compositionLocalOf<WindowSizeClass> { error("Not initialized") }