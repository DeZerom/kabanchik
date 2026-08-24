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
import ru.kabanchik.common.chat.model.CommonAttachment
import ru.kabanchik.common.filePicker.api.SelectedFile
import ru.kabanchik.pro.feature.chat.api.details.ProChatDetailsContract.Event
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class ProChatDetailsStoreTest {
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
