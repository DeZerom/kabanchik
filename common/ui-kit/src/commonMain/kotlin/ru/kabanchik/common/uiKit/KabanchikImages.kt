package ru.kabanchik.common.uiKit

import androidx.compose.runtime.Composable
import kabanchik.common.ui_kit.generated.resources.Res
import kabanchik.common.ui_kit.generated.resources.app_icon
import org.jetbrains.compose.resources.painterResource

object KabanchikImages {
    val AppImage
        @Composable get() = painterResource(Res.drawable.app_icon)
}