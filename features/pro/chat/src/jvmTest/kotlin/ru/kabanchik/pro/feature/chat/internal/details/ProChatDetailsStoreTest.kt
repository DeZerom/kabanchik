package ru.kabanchik.pro.feature.chat.internal.details

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
import kotlinx.io.Buffer
import ru.kabanchik.common.chat.model.CommonAttachment
import ru.kabanchik.common.chat.model.CommonChatMessage
import ru.kabanchik.common.chat.model.CommonMessage
import ru.kabanchik.common.chat.model.CommonMessageType
import ru.kabanchik.common.features.chat.logic.details.findFile
import ru.kabanchik.common.files.api.SelectedFile
import ru.kabanchik.pro.feature.chat.api.details.ProChatDetailsContract.Event
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class ProChatDetailsStoreTest {
    @Test
    fun releasesFileWhenItIsRemoved() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        try {
            var releaseCount = 0
            val store = createStore()
            runCurrent()
            store.handleEvent(Event.FilesSelected(listOf(selectedFile { releaseCount++ })))
            val fileId = store.currentState.selectedFiles.single().id

            store.handleEvent(Event.FileRemoved(fileId))

            assertTrue(store.currentState.selectedFiles.isEmpty())
            assertEquals(1, releaseCount)
            store.onDestroy()
        } finally {
            Dispatchers.resetMain()
        }
    }

    @Test
    fun marksFileAsLoadingUntilItIsOpened() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        try {
            val allowOpening = CompletableDeferred<Unit>()
            val store = ProChatDetailsStore(
                chatDetailsInteractor = FakeProChatDetailsInteractor(
                    upload = { attachment() },
                    messages = listOf(messageWithAttachment()),
                ),
                userInteractor = ProFakeUserInteractor,
                errorHandler = ProFakeErrorHandler,
                fileOpener = ProFakeFileOpener { _, _ -> allowOpening.await() },
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
    fun exposesSendingStateUntilFileAndMessageAreSent() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        try {
            val allowUpload = CompletableDeferred<Unit>()
            val store = ProChatDetailsStore(
                chatDetailsInteractor = FakeProChatDetailsInteractor(
                    upload = {
                        allowUpload.await()
                        attachment()
                    },
                ),
                userInteractor = ProFakeUserInteractor,
                errorHandler = ProFakeErrorHandler,
                fileOpener = ProFakeFileOpener(),
                sessionId = "",
                shouldReconnect = false,
            )
            runCurrent()
            store.handleEvent(Event.FilesSelected(listOf(selectedFile())))

            store.handleEvent(Event.MessageSent)
            runCurrent()

            assertTrue(store.currentState.isSending)
            allowUpload.complete(Unit)
            advanceUntilIdle()
            assertFalse(store.currentState.isSending)
            assertTrue(store.currentState.selectedFiles.isEmpty())
            store.onDestroy()
        } finally {
            Dispatchers.resetMain()
        }
    }

    private fun selectedFile(onRelease: () -> Unit = {}): SelectedFile {
        return SelectedFile(
            fileName = "document.pdf",
            contentType = "application/pdf",
            size = 1,
            previewUri = "file:///document.pdf",
            sourceProvider = { Buffer() },
            releaseAccess = onRelease,
        )
    }

    private fun createStore(): ProChatDetailsStore {
        return ProChatDetailsStore(
            chatDetailsInteractor = FakeProChatDetailsInteractor(upload = { attachment() }),
            userInteractor = ProFakeUserInteractor,
            errorHandler = ProFakeErrorHandler,
            fileOpener = ProFakeFileOpener(),
            sessionId = "",
            shouldReconnect = false,
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
