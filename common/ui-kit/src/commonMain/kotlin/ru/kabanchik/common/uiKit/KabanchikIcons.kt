package ru.kabanchik.common.uiKit

import androidx.compose.runtime.Composable
import kabanchik.common.ui_kit.generated.resources.Res
import kabanchik.common.ui_kit.generated.resources.close_24px
import kabanchik.common.ui_kit.generated.resources.send_24px
import org.jetbrains.compose.resources.painterResource

object KabanchikIcons {
    val Send24
        @Composable get() = painterResource(Res.drawable.send_24px)

    val Close24
        @Composable get() = painterResource(Res.drawable.close_24px)
}