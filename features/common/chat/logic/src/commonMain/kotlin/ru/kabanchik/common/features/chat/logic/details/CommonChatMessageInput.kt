package ru.kabanchik.common.features.chat.logic.details

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.decodeToImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.unit.dp
import kabanchik.features.common.chat.logic.generated.resources.Res
import kabanchik.features.common.chat.logic.generated.resources.chat_details_hint
import org.jetbrains.compose.resources.stringResource
import ru.kabanchik.common.feature.chat.model.CommonPendingFile
import ru.kabanchik.common.filePicker.api.SelectedFile
import ru.kabanchik.common.uiKit.theme.KabanchikTheme
import ru.kabanchik.common.uiKit.theme.bodyMedium
import ru.kabanchik.common.uiKit.widgets.CommonFilePreview
import ru.kabanchik.common.uiKit.widgets.CommonImageFilePreview

@Composable
internal fun CommonChatMessageInput(
    value: String,
    onValueChange: (String) -> Unit,
    selectedFiles: List<CommonPendingFile>,
    onFileRemoved: (CommonPendingFile) -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val shape = RoundedCornerShape(20.dp)
    val borderColor = if (isFocused) {
        KabanchikTheme.colors.accent
    } else {
        MaterialTheme.colorScheme.outline
    }

    Column(
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .clip(shape)
            .background(KabanchikTheme.colors.background)
            .border(width = 1.dp, color = borderColor, shape = shape)
            .heightIn(min = 48.dp)
    ) {
        if (selectedFiles.isNotEmpty()) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
            ) {
                items(
                    items = selectedFiles,
                    key = { it.id },
                ) { pendingFile ->
                    PendingFilePreview(
                        pendingFile = pendingFile,
                        onRemove = { onFileRemoved(pendingFile) },
                    )
                }
            }
        }

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = KabanchikTheme.typography.bodyMedium.copy(
                color = KabanchikTheme.colors.mainText,
            ),
            cursorBrush = SolidColor(KabanchikTheme.colors.accent),
            interactionSource = interactionSource,
            decorationBox = { innerTextField ->
                Box {
                    if (value.isEmpty()) {
                        Text(
                            text = stringResource(Res.string.chat_details_hint),
                            color = KabanchikTheme.colors.secondaryText,
                            style = KabanchikTheme.typography.bodyMedium,
                        )
                    }
                    innerTextField()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
        )
    }
}

@Composable
private fun PendingFilePreview(
    pendingFile: CommonPendingFile,
    onRemove: () -> Unit,
) {
    val file = pendingFile.file
    val imagePainter = remember(file) {
        if (file.isImage()) {
            runCatching { BitmapPainter(file.bytes.decodeToImageBitmap()) }.getOrNull()
        } else {
            null
        }
    }

    if (imagePainter != null) {
        CommonImageFilePreview(
            painter = imagePainter,
            onRemove = onRemove,
            contentDescription = file.fileName,
        )
    } else {
        CommonFilePreview(
            fileName = file.fileName,
            contentType = file.contentType,
            fileSize = file.size,
            onRemove = onRemove,
        )
    }
}

private fun SelectedFile.isImage(): Boolean {
    return contentType.substringBefore(';').trim().startsWith("image/", ignoreCase = true)
}
