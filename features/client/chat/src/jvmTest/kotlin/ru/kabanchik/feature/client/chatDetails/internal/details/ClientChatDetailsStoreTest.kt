package ru.kabanchik.feature.client.chatDetails.internal.details

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import ru.kabanchik.common.chat.model.CommonAttachment
import ru.kabanchik.common.filePicker.api.SelectedFile
import ru.kabanchik.feature.client.chatDetails.api.details.ChatDetailsContract.Event
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class ClientChatDetailsStoreTest {
    @Test
    fun clearsSelectedFilesWhenMessageIsSent() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        try {
            val store = ClientChatDetailsStore(
                chatDetailsInteractor = FakeClientChatDetailsInteractor(
                    upload = { attachment() },
                ),
                userInteractor = FakeUserInteractor,
                errorHandler = FakeErrorHandler,
                sessionId = "",
                shouldReconnect = false,
            )
            runCurrent()
            store.handleEvent(Event.FilesSelected(listOf(selectedFile())))

            store.handleEvent(Event.MessageSent)
            advanceUntilIdle()

            assertFalse(store.currentState.isSending)
            assertTrue(store.currentState.selectedFiles.isEmpty())
            store.onDestroy()
        } finally {
            Dispatchers.resetMain()
        }
    }

    @Test
    fun resetsSendingStateAndKeepsFileWhenUploadFails() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        try {
            val store = ClientChatDetailsStore(
                chatDetailsInteractor = FakeClientChatDetailsInteractor(
                    upload = { error("Upload failed") },
                ),
                userInteractor = FakeUserInteractor,
                errorHandler = FakeErrorHandler,
                sessionId = "",
                shouldReconnect = false,
            )
            runCurrent()
            store.handleEvent(Event.FilesSelected(listOf(selectedFile())))

            store.handleEvent(Event.MessageSent)

            assertTrue(store.currentState.isSending)
            advanceUntilIdle()
            assertFalse(store.currentState.isSending)
            assertTrue(store.currentState.selectedFiles.isNotEmpty())
            store.onDestroy()
        } finally {
            Dispatchers.resetMain()
        }
    }

    private fun selectedFile(): SelectedFile {
        return SelectedFile(
            fileName = "document.pdf",
            contentType = "application/pdf",
            size = 1,
            bytes = byteArrayOf(1),
        )
    }

    private fun attachment(): CommonAttachment {
        return CommonAttachment(
            fileId = "file-id",
            originalName = "document.pdf",
            contentType = "application/pdf",
            size = 1,
            downloadUrl = "",
        )
    }
}
