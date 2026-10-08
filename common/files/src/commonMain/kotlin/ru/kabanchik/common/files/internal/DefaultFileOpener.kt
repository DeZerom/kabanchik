package ru.kabanchik.common.files.internal

import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.atomicMove
import io.github.vinceglb.filekit.cacheDir
import io.github.vinceglb.filekit.createDirectories
import io.github.vinceglb.filekit.delete
import io.github.vinceglb.filekit.div
import io.github.vinceglb.filekit.exists
import io.github.vinceglb.filekit.path
import io.github.vinceglb.filekit.sink
import io.github.vinceglb.filekit.dialogs.openFileWithDefaultApplication
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsChannel
import io.ktor.utils.io.readAvailable
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.withContext
import kotlinx.io.buffered
import kotlinx.io.write
import ru.kabanchik.common.files.api.FileOpener
import ru.kabanchik.common.files.api.FileOpeningException

internal class DefaultFileOpener(
    private val httpClient: HttpClient,
) : FileOpener {
    override suspend fun open(
        url: String,
        fileName: String,
    ) {
        try {
            require(url.isNotBlank()) { "File URL must not be blank" }

            val directory = FileKit.cacheDir / CacheDirectory
            directory.createDirectories()

            val destination = directory / cachedFileName(url = url, fileName = fileName)
            if (!destination.exists()) {
                download(url = url, destination = destination)
            }

            FileKit.openFileWithDefaultApplication(destination)
        } catch (error: CancellationException) {
            throw error
        } catch (error: Throwable) {
            throw FileOpeningException(error)
        }
    }

    private suspend fun download(
        url: String,
        destination: PlatformFile,
    ) {
        val temporaryFile = PlatformFile("${destination.path}.part")
        try {
            val response = httpClient.get(url)
            check(response.status.value in SuccessStatusRange) {
                "File download failed with status ${response.status.value}"
            }
            val channel = response.bodyAsChannel()
            withContext(Dispatchers.IO) {
                temporaryFile.sink().buffered().use { sink ->
                    val buffer = ByteArray(DownloadBufferSize)
                    while (true) {
                        val bytesRead = channel.readAvailable(buffer)
                        if (bytesRead == -1) break
                        sink.write(buffer, startIndex = 0, endIndex = bytesRead)
                    }
                }
            }
            temporaryFile.atomicMove(destination)
        } catch (error: CancellationException) {
            temporaryFile.deleteSafely()
            throw error
        } catch (error: Throwable) {
            temporaryFile.deleteSafely()
            throw error
        }
    }
}

internal fun cachedFileName(url: String, fileName: String): String {
    val safeFileName = fileName
        .replace(UnsafeFileNameCharacters, "_")
        .trim()
        .ifBlank { DefaultFileName }
        .preserveExtensionWhenTruncated()
    return "${url.hashCode()}_$safeFileName"
}

private fun String.preserveExtensionWhenTruncated(): String {
    if (length <= MaxFileNameLength) return this

    val extension = substringAfterLast('.', missingDelimiterValue = "")
        .takeIf { it.length in 1..MaxPreservedExtensionLength }
        ?: return take(MaxFileNameLength)
    val suffix = ".$extension"
    return substringBeforeLast('.')
        .take(MaxFileNameLength - suffix.length)
        .plus(suffix)
}

private suspend fun PlatformFile.deleteSafely() {
    withContext(NonCancellable) {
        runCatching { delete(mustExist = false) }
    }
}

private const val CacheDirectory = "opened-files"
private const val DefaultFileName = "file"
private const val DownloadBufferSize = 8 * 1024
private const val MaxFileNameLength = 180
private const val MaxPreservedExtensionLength = 16
private val SuccessStatusRange = 200..299
private val UnsafeFileNameCharacters = Regex("[\\\\/:*?\"<>|\\u0000-\\u001F]")
