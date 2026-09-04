package ru.kabanchik.common.files.internal

import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.absolutePath

internal actual fun PlatformFile.toPreviewUri(): String = absolutePath()
