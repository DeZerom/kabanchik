package ru.kabanchik.common.uiKit.icons.extensions

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import ru.kabanchik.common.uiKit.icons.KabanchikIcons

val KabanchikIcons.EyeOff16: ImageVector
    get() = ImageVector.Builder(
        name = "EyeOff16",
        defaultWidth = 16.dp,
        defaultHeight = 16.dp,
        viewportWidth = 14f,
        viewportHeight = 14f
    ).apply {
        path(
            fill = SolidColor(Color.Transparent),
            stroke = SolidColor(Color(0xFFB4B4B4)),
            strokeLineWidth = 2f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
            pathFillType = PathFillType.NonZero
        ) {
            moveTo(10.1207f, 10.1153f)
            curveTo(9.18575f, 10.701f, 8.10319f, 11.0079f, 7f, 11f)
            curveTo(4.6f, 11f, 2.6f, 9.66667f, 1f, 7f)
            curveTo(1.848f, 5.58667f, 2.808f, 4.548f, 3.88f, 3.884f)

            moveTo(5.78667f, 3.12f)
            curveTo(6.18599f, 3.0389f, 6.59253f, 2.9987f, 7f, 3f)
            curveTo(9.4f, 3f, 11.4f, 4.33333f, 13f, 7f)
            curveTo(12.5556f, 7.74f, 12.0804f, 8.37778f, 11.5747f, 8.91333f)

            moveTo(1f, 1f)
            lineTo(13f, 13f)
        }
    }.build()
