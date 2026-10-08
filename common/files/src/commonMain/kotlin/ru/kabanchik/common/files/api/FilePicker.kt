package ru.kabanchik.common.files.api

interface FilePicker {
    suspend fun pickFiles(): List<SelectedFile>
}
