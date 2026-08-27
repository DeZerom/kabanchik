package ru.kabanchik.feature.client.chatDetails.internal.details

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.datetime.LocalDateTime
import ru.kabanchik.common.chat.model.CommonAttachment
import ru.kabanchik.common.chat.model.CommonChatMessage
import ru.kabanchik.common.chat.model.CommonMessage
import ru.kabanchik.common.chat.model.CommonMessageType
import ru.kabanchik.common.features.chat.logic.details.findFile
import ru.kabanchik.common.files.api.SelectedFile
import ru.kabanchik.feature.client.chatDetails.api.details.ChatDetailsContract.Event
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class ClientChatDetailsStoreTest {
    @Test
    fun marksFileAsLoadingUntilItIsOpened() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        try {
            val allowOpening = CompletableDeferred<Unit>()
            val store = ClientChatDetailsStore(
                chatDetailsInteractor = FakeClientChatDetailsInteractor(
                    upload = { attachment() },
                    messages = listOf(messageWithAttachment()),
                ),
                userInteractor = FakeUserInteractor,
                errorHandler = FakeErrorHandler,
                fileOpener = FakeFileOpener { _, _ -> allowOpening.await() },
                sessionId = "session-id",
                shouldReconnect = false,
            )
            runCurrent()

            store.handleEvent(Event.FileOpenRequested(fileId = "file-id"))
            runCurrent()

            assertTrue(store.currentState.messages.findFile("file-id")?.isLoading == true)

            allowOpening.complete(Unit)
            advanceUntilIdle()

            assertFalse(store.currentState.messages.findFile("file-id")?.isLoading == true)
            store.onDestroy()
        } finally {
            Dispatchers.resetMain()
        }
    }

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
                fileOpener = FakeFileOpener(),
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
                fileOpener = FakeFileOpener(),
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

    private fun messageWithAttachment(): CommonChatMessage {
        return CommonChatMessage.Message(
            CommonMessage(
                id = "message-id",
                sessionId = "session-id",
                authorLogin = "user",
                text = "",
                type = CommonMessageType.File,
                attachments = listOf(attachment()),
                time = LocalDateTime(2026, 1, 1, 12, 0),
            )
        )
    }
}
