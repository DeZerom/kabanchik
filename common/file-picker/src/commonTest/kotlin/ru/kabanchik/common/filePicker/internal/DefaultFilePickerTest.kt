package ru.kabanchik.common.filePicker.internal

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class DefaultFilePickerTest {
    @Test
    fun acceptsAllowedExtensionsIgnoringCase() {
        assertTrue("photo.JPG".hasAllowedFileExtension())
        assertTrue("report.docx".hasAllowedFileExtension())
        assertTrue("voice.M4A".hasAllowedFileExtension())
        assertTrue("video.mp4".hasAllowedFileExtension())
    }

    @Test
    fun rejectsDisallowedExtensions() {
        assertFalse("archive.rar".hasAllowedFileExtension())
        assertFalse("document.doc".hasAllowedFileExtension())
    }

    @Test
    fun usesLastExtensionWhenFileNameContainsDots() {
        assertTrue("doc.doc.docx".hasAllowedFileExtension())
        assertTrue("archive.rar.zip".hasAllowedFileExtension())
        assertFalse("document.pdf.exe".hasAllowedFileExtension())
    }

    @Test
    fun rejectsFilesWithoutExtension() {
        assertFalse("README".hasAllowedFileExtension())
    }
}
