package ru.kabanchik.common.uiKit.icons.extensions

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import ru.kabanchik.common.uiKit.icons.KabanchikIcons

val KabanchikIcons.Send24: ImageVector
    get() = ImageVector.Builder(
        name = "Send24",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(
            fill = SolidColor(Color(0xFFC56300))
        ) {
            moveTo(20.235f, 5.68609f)
            curveTo(20.667f, 4.49109f, 19.509f, 3.33309f, 18.314f, 3.76609f)
            lineTo(3.70904f, 9.04809f)
            curveTo(2.51004f, 9.48209f, 2.36504f, 11.1181f, 3.46804f, 11.7571f)
            lineTo(8.13004f, 14.4561f)
            lineTo(12.293f, 10.2931f)
            curveTo(12.4816f, 10.1109f, 12.7342f, 10.0101f, 12.9964f, 10.0124f)
            curveTo(13.2586f, 10.0147f, 13.5095f, 10.1199f, 13.6949f, 10.3053f)
            curveTo(13.8803f, 10.4907f, 13.9854f, 10.7415f, 13.9877f, 11.0037f)
            curveTo(13.99f, 11.2659f, 13.8892f, 11.5185f, 13.707f, 11.7071f)
            lineTo(9.54404f, 15.8701f)
            lineTo(12.244f, 20.5321f)
            curveTo(12.882f, 21.6351f, 14.518f, 21.4891f, 14.952f, 20.2911f)
            lineTo(20.235f, 5.68609f)
            close()
        }
    }.build()
