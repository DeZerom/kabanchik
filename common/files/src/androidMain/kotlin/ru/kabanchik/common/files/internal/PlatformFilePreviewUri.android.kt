package ru.kabanchik.common.files.internal

import android.net.Uri
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.absolutePath
import java.io.File

internal actual fun PlatformFile.toPreviewUri(): String {
    val path = absolutePath()
    return if (path.contains("://")) path else Uri.fromFile(File(path)).toString()
}
