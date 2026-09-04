package ru.kabanchik.common.files.internal

import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.absolutePath
import java.io.File

internal actual fun PlatformFile.toPreviewUri(): String = File(absolutePath()).toURI().toString()
