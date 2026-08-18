package ru.kabanchik.common.filePicker.api

class SelectedFile(
    val fileName: String,
    val contentType: String,
    val size: Long,
    val bytes: ByteArray,
)
