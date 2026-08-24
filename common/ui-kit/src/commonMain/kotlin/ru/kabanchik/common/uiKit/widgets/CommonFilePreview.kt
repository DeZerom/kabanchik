package ru.kabanchik.common.uiKit.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kabanchik.common.ui_kit.generated.resources.Res
import kabanchik.common.ui_kit.generated.resources.file_preview_remove
import kabanchik.common.ui_kit.generated.resources.file_size_unit_bytes
import kabanchik.common.ui_kit.generated.resources.file_size_unit_gigabytes
import kabanchik.common.ui_kit.generated.resources.file_size_unit_kilobytes
import kabanchik.common.ui_kit.generated.resources.file_size_unit_megabytes
import org.jetbrains.compose.resources.stringResource
import ru.kabanchik.common.uiKit.icons.KabanchikIcons
import ru.kabanchik.common.uiKit.theme.KabanchikTheme
import ru.kabanchik.common.uiKit.theme.extraSmallText
import kotlin.math.round

@Composable
fun CommonFilePreview(
    fileName: String,
    contentType: String,
    fileSize: Long,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val fileType = formatFileType(contentType, fileName)
    val displayName = formatFileName(fileName)

    Box(
        modifier = modifier.size(PreviewContainerSize)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .size(PreviewContentSize)
                .align(Alignment.BottomStart)
                .clip(RoundedCornerShape(PreviewCornerRadius))
                .background(KabanchikTheme.colors.accent.copy(alpha = 0.72f))
                .padding(6.dp)
        ) {
            Icon(
                painter = KabanchikIcons.File16,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = displayName,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = KabanchikTheme.typography.extraSmallText,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = "$fileType, ${formatFileSize(fileSize)}",
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = KabanchikTheme.typography.extraSmallText.copy(fontSize = 7.sp),
                modifier = Modifier.fillMaxWidth()
            )
        }
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

internal fun formatFileType(contentType: String, fileName: String): String {
    val normalizedContentType = contentType.substringBefore(';').trim().lowercase()
    return MimeTypeExtensions[normalizedContentType]
        ?: fileName.substringAfterLast('.', missingDelimiterValue = "")
            .takeIf { it.isNotBlank() }
            ?.uppercase()
        ?: UnknownFileType
}

internal fun formatFileName(fileName: String): String {
    val nameWithoutExtension = fileName.substringBeforeLast('.', missingDelimiterValue = fileName)
    return nameWithoutExtension.ifBlank { fileName }
}

@Composable
internal fun formatFileSize(sizeInBytes: Long): String {
    val formattedSize = remember(sizeInBytes) {
        val safeSize = sizeInBytes.coerceAtLeast(0L)
        if (safeSize < BytesInKilobyte) {
            FormattedFileSize(safeSize.toString(), FileSizeUnit.Bytes)
        } else {
            var value = safeSize.toDouble() / BytesInKilobyte
            var unit = FileSizeUnit.Kilobytes
            while (value >= BytesInKilobyte && unit != FileSizeUnit.Gigabytes) {
                value /= BytesInKilobyte
                unit = unit.next()
            }
            FormattedFileSize(formatDecimal(value), unit)
        }
    }

    val unit = stringResource(
        when (formattedSize.unit) {
            FileSizeUnit.Bytes -> Res.string.file_size_unit_bytes
            FileSizeUnit.Kilobytes -> Res.string.file_size_unit_kilobytes
            FileSizeUnit.Megabytes -> Res.string.file_size_unit_megabytes
            FileSizeUnit.Gigabytes -> Res.string.file_size_unit_gigabytes
        }
    )
    return "${formattedSize.value} $unit"
}

private fun formatDecimal(value: Double): String {
    val roundedValue = round(value * 100) / 100
    return roundedValue.toString()
        .removeSuffix(".0")
        .replace('.', ',')
}

@Preview
@Composable
private fun CommonFilePreviewPreview() {
    KabanchikTheme {
        CommonFilePreview(
            fileName = "Длинное название документа.pdf",
            contentType = "application/pdf",
            fileSize = 3_270_246,
            onRemove = {},
        )
    }
}

private const val UnknownFileType = "FILE"
private const val BytesInKilobyte = 1024L

private val MimeTypeExtensions = mapOf(
    "image/jpeg" to "JPG",
    "image/png" to "PNG",
    "image/gif" to "GIF",
    "image/webp" to "WEBP",
    "application/pdf" to "PDF",
    "application/vnd.openxmlformats-officedocument.wordprocessingml.document" to "DOCX",
    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" to "XLSX",
    "application/vnd.openxmlformats-officedocument.presentationml.presentation" to "PPTX",
    "text/plain" to "TXT",
    "text/markdown" to "MD",
    "text/csv" to "CSV",
    "application/zip" to "ZIP",
    "application/x-zip-compressed" to "ZIP",
    "audio/mpeg" to "MP3",
    "audio/mp4" to "M4A",
    "audio/x-m4a" to "M4A",
    "audio/ogg" to "OGG",
    "application/ogg" to "OGG",
    "audio/wav" to "WAV",
    "audio/x-wav" to "WAV",
    "audio/wave" to "WAV",
    "video/mp4" to "MP4",
)

private data class FormattedFileSize(
    val value: String,
    val unit: FileSizeUnit,
)

private enum class FileSizeUnit {
    Bytes,
    Kilobytes,
    Megabytes,
    Gigabytes;

    fun next(): FileSizeUnit = when (this) {
        Bytes -> Kilobytes
        Kilobytes -> Megabytes
        Megabytes, Gigabytes -> Gigabytes
    }
}

private val PreviewContainerSize = 72.dp
private val PreviewContentSize = 64.dp
private val PreviewCornerRadius = 14.dp
private val RemoveButtonSize = 24.dp
private val RemoveIconSize = 16.dp
