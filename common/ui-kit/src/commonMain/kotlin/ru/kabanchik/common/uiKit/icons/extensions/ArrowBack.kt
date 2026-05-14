package ru.kabanchik.common.uiKit.icons.extensions

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import ru.kabanchik.common.uiKit.icons.KabanchikIcons

val KabanchikIcons.ArrowBack24: ImageVector
    get() = ImageVector.Builder(
        name = "ArrowBack24",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 960f,
        viewportHeight = 960f
    ).apply {
        path(
            fill = SolidColor(Color.Black)
        ) {
            moveTo(400f, 880f)
            lineTo(0f, 480f)
            lineTo(400f, 80f)
            lineTo(456f, 137f)
            lineTo(153f, 440f)
            horizontalLineTo(880f)
            verticalLineTo(520f)
            horizontalLineTo(153f)
            lineTo(456f, 823f)
            close()
        }
    }.build()