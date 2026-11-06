package ru.kabanchik.common.files.api

interface FileOpener {
    suspend fun open(
        url: String,
        fileName: String,
    )
}
