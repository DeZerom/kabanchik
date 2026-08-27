package ru.kabanchik.common.features.chat.logic.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import dev.shivathapaa.logger.api.Log
import ru.kabanchik.common.feature.chat.model.CommonUiAttachment
import ru.kabanchik.common.uiKit.theme.KabanchikTheme
import ru.kabanchik.common.uiKit.widgets.CommonChatFileItem

@Composable
internal fun CommonChatAttachments(
    attachments: List<CommonUiAttachment>,
    onFileClicked: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val images = attachments.filterIsInstance<CommonUiAttachment.Image>()
    val files = attachments.filterIsInstance<CommonUiAttachment.File>()
    if (images.isEmpty() && files.isEmpty()) return

    Column(
        verticalArrangement = Arrangement.spacedBy(AttachmentSectionSpacing),
        modifier = modifier.fillMaxWidth()
    ) {
        if (images.isNotEmpty()) {
            Column(verticalArrangement = Arrangement.spacedBy(ImageSpacing)) {
                images.toImageRows().forEach { row ->
                    if (row.size == 1) {
                        CommonChatImage(
                            image = row.first(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(FullWidthImageAspectRatio)
                        )
                    } else {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(ImageSpacing),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            row.forEach { image ->
                                CommonChatImage(
                                    image = image,
                                    modifier = Modifier
                                        .weight(1f)
                                        .aspectRatio(GridImageAspectRatio)
                                )
                            }
                        }
                    }
                }
            }
        }
        if (files.isNotEmpty()) {
            Column(
                verticalArrangement = Arrangement.spacedBy(FileSpacing),
                modifier = Modifier.padding(horizontal = FileHorizontalPadding),
            ) {
                files.forEach { file ->
                    CommonChatFileItem(
                        fileName = file.originalName,
                        fileSize = file.size,
                        onClick = { onFileClicked(file.fileId) },
                        isLoading = file.isLoading,
                    )
                }
            }
        }
    }
}

@Composable
private fun CommonChatImage(
    image: CommonUiAttachment.Image,
    modifier: Modifier = Modifier,
) {
    SubcomposeAsyncImage(
        model = image.downloadUrl,
        contentDescription = image.originalName,
        contentScale = ContentScale.Crop,
        loading = { ImagePlaceholder() },
        error = {
            Log.w(it.toString(), "ChatImage")
            ImagePlaceholder()
        },
        modifier = modifier.clip(RoundedCornerShape(ImageCornerRadius))
    )
}

@Composable
private fun ImagePlaceholder() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(KabanchikTheme.colors.secondaryText.copy(alpha = 0.12f))
    )
}

internal fun <T> List<T>.toImageRows(): List<List<T>> {
    if (isEmpty()) return emptyList()

    val fullWidthImageCount = if (size == 1 || size % 2 != 0) 1 else 0
    return buildList {
        if (fullWidthImageCount == 1) {
            add(listOf(this@toImageRows.first()))
        }
        addAll(this@toImageRows.drop(fullWidthImageCount).chunked(size = ImagesPerRow))
    }
}

private const val ImagesPerRow = 2
private const val FullWidthImageAspectRatio = 4f / 3f
private const val GridImageAspectRatio = 1f
private val ImageSpacing = 4.dp
private val ImageCornerRadius = 12.dp
private val AttachmentSectionSpacing = 12.dp
private val FileSpacing = 8.dp
private val FileHorizontalPadding = 16.dp
