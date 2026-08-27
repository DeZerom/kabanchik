package ru.kabanchik.common.files.api

class SelectedFile(
    val fileName: String,
    val contentType: String,
    val size: Long,
    val bytes: ByteArray,
)
