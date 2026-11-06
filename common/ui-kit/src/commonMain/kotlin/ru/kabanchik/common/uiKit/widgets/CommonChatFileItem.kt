package ru.kabanchik.common.uiKit.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.kabanchik.common.uiKit.HSpacer
import ru.kabanchik.common.uiKit.theme.KabanchikTheme
import ru.kabanchik.common.uiKit.theme.bodyS
import ru.kabanchik.common.uiKit.theme.extraSmallText

@Composable
fun CommonChatFileItem(
    fileName: String,
    fileSize: Long,
    onClick: () -> Unit,
    isLoading: Boolean = false,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(FileIconContainerSize)
                .clip(RoundedCornerShape(FileIconCornerRadius))
                .background(
                    KabanchikTheme.colors.accent.copy(alpha = CommonFileBackgroundAlpha)
                ),
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = Color.White,
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(24.dp),
                )
            } else {
                CommonFileIcon(size = FileIconSize)
            }
        }
        HSpacer(12.dp)
        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp),
            modifier = Modifier.weight(1f),
        ) {
            Text(
                text = fileName,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = KabanchikTheme.typography.bodyS,
                modifier = Modifier.fillMaxWidth(),
            )
            Text(
                text = formatFileSize(fileSize),
                color = KabanchikTheme.colors.secondaryText,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = KabanchikTheme.typography.extraSmallText,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Preview
@Composable
private fun CommonChatFileItemPreview() {
    KabanchikTheme {
        CommonChatFileItem(
            fileName = "Очень длинное название документа.pdf",
            fileSize = 3_270_246,
            onClick = {},
            modifier = Modifier.width(280.dp),
        )
    }
}

private val FileIconSize = 32.dp
private val FileIconContainerSize = 48.dp
private val FileIconCornerRadius = 12.dp
