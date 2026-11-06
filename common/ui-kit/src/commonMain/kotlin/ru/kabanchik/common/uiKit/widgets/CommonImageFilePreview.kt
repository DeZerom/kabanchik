package ru.kabanchik.common.uiKit.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kabanchik.common.ui_kit.generated.resources.Res
import kabanchik.common.ui_kit.generated.resources.file_preview_remove
import org.jetbrains.compose.resources.stringResource
import ru.kabanchik.common.uiKit.KabanchikImages
import ru.kabanchik.common.uiKit.icons.KabanchikIcons
import ru.kabanchik.common.uiKit.theme.KabanchikTheme

@Composable
fun CommonImageFilePreview(
    painter: Painter,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
) {
    Box(
        modifier = modifier.size(PreviewContainerSize)
    ) {
        Image(
            painter = painter,
            contentDescription = contentDescription,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(PreviewContentSize)
                .align(Alignment.BottomStart)
                .clip(RoundedCornerShape(PreviewCornerRadius))
        )
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(RemoveButtonSize)
                .align(Alignment.TopEnd)
                .clip(CircleShape)
                .background(KabanchikTheme.colors.accent)
                .clickable(
                    role = Role.Button,
                    onClickLabel = stringResource(Res.string.file_preview_remove),
                    onClick = onRemove,
                )
        ) {
            Icon(
                painter = KabanchikIcons.Close24,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(RemoveIconSize)
            )
        }
    }
}

@Preview
@Composable
private fun CommonImageFilePreviewPreview() {
    KabanchikTheme {
        CommonImageFilePreview(
            painter = KabanchikImages.AppImage,
            onRemove = {},
            contentDescription = "Изображение",
        )
    }
}

private val PreviewContainerSize = 72.dp
private val PreviewContentSize = 64.dp
private val PreviewCornerRadius = 14.dp
private val RemoveButtonSize = 24.dp
private val RemoveIconSize = 16.dp
