package ru.kabanchik.common.uiKit.widgets

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kabanchik.common.ui_kit.generated.resources.Res
import kabanchik.common.ui_kit.generated.resources.file_size_unit_bytes
import kabanchik.common.ui_kit.generated.resources.file_size_unit_gigabytes
import kabanchik.common.ui_kit.generated.resources.file_size_unit_kilobytes
import kabanchik.common.ui_kit.generated.resources.file_size_unit_megabytes
import org.jetbrains.compose.resources.stringResource
import kotlin.math.round

@Composable
internal fun formatFileSize(sizeInBytes: Long): String {
    val formattedSize = remember(sizeInBytes) {
        formatFileSizeValue(sizeInBytes)
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

internal fun formatFileSizeValue(sizeInBytes: Long): FormattedFileSize {
    val safeSize = sizeInBytes.coerceAtLeast(0L)
    if (safeSize < BytesInKilobyte) {
        return FormattedFileSize(safeSize.toString(), FileSizeUnit.Bytes)
    }

    var value = safeSize.toDouble() / BytesInKilobyte
    var unit = FileSizeUnit.Kilobytes
    while (value >= BytesInKilobyte && unit != FileSizeUnit.Gigabytes) {
        value /= BytesInKilobyte
        unit = unit.next()
    }
    return FormattedFileSize(formatDecimal(value), unit)
}

private fun formatDecimal(value: Double): String {
    val roundedValue = round(value * 100) / 100
    return roundedValue.toString()
        .removeSuffix(".0")
        .replace('.', ',')
}

internal data class FormattedFileSize(
    val value: String,
    val unit: FileSizeUnit,
)

internal enum class FileSizeUnit {
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

private const val BytesInKilobyte = 1024L
