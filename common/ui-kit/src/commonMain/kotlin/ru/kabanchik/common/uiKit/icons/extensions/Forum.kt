package ru.kabanchik.common.uiKit.icons.extensions

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import ru.kabanchik.common.uiKit.icons.KabanchikIcons

val KabanchikIcons.Forum96: ImageVector
    get() = ImageVector.Builder(
        name = "Forum96",
        defaultWidth = 96.dp,
        defaultHeight = 96.dp,
        viewportWidth = 96f,
        viewportHeight = 96f
    ).apply {
        path(
            fill = SolidColor(Color.Black)
        ) {
            moveTo(84f, 24f)
            horizontalLineTo(76f)
            verticalLineTo(60f)
            horizontalLineTo(28f)
            verticalLineTo(68f)
            curveTo(28f, 70.2f, 29.8f, 72f, 32f, 72f)
            horizontalLineTo(72f)
            lineTo(88f, 88f)
            verticalLineTo(28f)
            curveTo(88f, 25.8f, 86.2f, 24f, 84f, 24f)

            moveTo(68f, 48f)
            verticalLineTo(12f)
            curveTo(68f, 9.8f, 66.2f, 8f, 64f, 8f)
            horizontalLineTo(12f)
            curveTo(9.8f, 8f, 8f, 9.8f, 8f, 12f)
            verticalLineTo(68f)
            lineTo(24f, 52f)
            horizontalLineTo(64f)
            curveTo(66.2f, 52f, 68f, 50.2f, 68f, 48f)
        }
    }.build()