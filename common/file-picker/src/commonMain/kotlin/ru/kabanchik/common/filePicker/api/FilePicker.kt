package ru.kabanchik.common.filePicker.api

interface FilePicker {
    suspend fun pickFiles(): List<SelectedFile>
}
