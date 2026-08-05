package ru.kabanchik.common.uiKit.icons.extensions

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import ru.kabanchik.common.uiKit.icons.KabanchikIcons

val KabanchikIcons.Eye16: ImageVector
    get() = ImageVector.Builder(
        name = "Eye16",
        defaultWidth = 16.dp,
        defaultHeight = 16.dp,
        viewportWidth = 22f,
        viewportHeight = 22f
    ).apply {
        path(
            fill = SolidColor(Color.Black)
        ) {
            moveTo(11f, 8f)
            curveTo(11.7956f, 8f, 12.5587f, 8.31607f, 13.1213f, 8.87868f)
            curveTo(13.6839f, 9.44129f, 14f, 10.2044f, 14f, 11f)
            curveTo(14f, 11.7956f, 13.6839f, 12.5587f, 13.1213f, 13.1213f)
            curveTo(12.5587f, 13.6839f, 11.7956f, 14f, 11f, 14f)
            curveTo(10.2044f, 14f, 9.44129f, 13.6839f, 8.87868f, 13.1213f)
            curveTo(8.31607f, 12.5587f, 8f, 11.7956f, 8f, 11f)
            curveTo(8f, 10.2044f, 8.31607f, 9.44129f, 8.87868f, 8.87868f)
            curveTo(9.44129f, 8.31607f, 10.2044f, 8f, 11f, 8f)

            moveTo(11f, 3.5f)
            curveTo(16f, 3.5f, 20.27f, 6.61f, 22f, 11f)
            curveTo(20.27f, 15.39f, 16f, 18.5f, 11f, 18.5f)
            curveTo(6f, 18.5f, 1.73f, 15.39f, 0f, 11f)
            curveTo(1.73f, 6.61f, 6f, 3.5f, 11f, 3.5f)

            moveTo(2.18f, 11f)
            curveTo(2.98825f, 12.6503f, 4.24331f, 14.0407f, 5.80248f, 15.0133f)
            curveTo(7.36165f, 15.9858f, 9.1624f, 16.5013f, 11f, 16.5013f)
            curveTo(12.8376f, 16.5013f, 14.6383f, 15.9858f, 16.1975f, 15.0133f)
            curveTo(17.7567f, 14.0407f, 19.0117f, 12.6503f, 19.82f, 11f)
            curveTo(19.0117f, 9.34969f, 17.7567f, 7.95925f, 16.1975f, 6.98675f)
            curveTo(14.6383f, 6.01424f, 12.8376f, 5.49868f, 11f, 5.49868f)
            curveTo(9.1624f, 5.49868f, 7.36165f, 6.01424f, 5.80248f, 6.98675f)
            curveTo(4.24331f, 7.95925f, 2.98825f, 9.34969f, 2.18f, 11f)
        }
    }.build()
