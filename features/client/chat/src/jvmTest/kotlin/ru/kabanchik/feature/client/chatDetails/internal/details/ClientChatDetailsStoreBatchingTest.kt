package ru.kabanchik.feature.client.chatDetails.internal.details

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.io.Buffer
import ru.kabanchik.common.chat.model.CommonAttachment
import ru.kabanchik.common.files.api.SelectedFile
import ru.kabanchik.feature.client.chatDetails.api.details.ChatDetailsContract.Event
import ru.kabanchik.feature.client.chatDetails.api.details.ChatDetailsContract.SideEffect
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class ClientChatDetailsStoreBatchingTest {
    @Test
    fun sendsUpToTenFilesInSingleMessage() = runStoreTest {
        val interactor = FakeClientChatDetailsInteractor(upload = ::attachment)
        val store = createStore(interactor)

        store.handleEvent(Event.MessageTextChanged("text"))
        store.handleEvent(Event.FilesSelected(selectedFiles(10)))
        store.handleEvent(Event.MessageSent)
        advanceUntilIdle()

        assertEquals(
            listOf(FakeClientChatDetailsInteractor.SentMessage("text", fileIds(0 until 10))),
            interactor.sentMessages,
        )
        assertTrue(store.currentState.selectedFiles.isEmpty())
        assertEquals("", store.currentState.currentMessage)
        store.onDestroy()
    }

    @Test
    fun splitsElevenFilesIntoTwoMessages() = runStoreTest {
        val interactor = FakeClientChatDetailsInteractor(upload = ::attachment)
        val store = createStore(interactor)

        store.handleEvent(Event.FilesSelected(selectedFiles(11)))
        store.handleEvent(Event.MessageSent)
        advanceUntilIdle()

        assertEquals(
            listOf(fileIds(0 until 10), fileIds(10 until 11)),
            interactor.sentMessages.map { it.attachmentIds },
        )
        assertTrue(interactor.sentMessages.all { it.content == null })
        assertTrue(store.currentState.selectedFiles.isEmpty())
        store.onDestroy()
    }

    @Test
    fun sendsTextOnlyWithFirstBatch() = runStoreTest {
        val interactor = FakeClientChatDetailsInteractor(upload = ::attachment)
        val store = createStore(interactor)

        store.handleEvent(Event.MessageTextChanged("text"))
        store.handleEvent(Event.FilesSelected(selectedFiles(25)))
        store.handleEvent(Event.MessageSent)
        advanceUntilIdle()

        assertEquals(
            listOf(
                FakeClientChatDetailsInteractor.SentMessage("text", fileIds(0 until 10)),
                FakeClientChatDetailsInteractor.SentMessage(null, fileIds(10 until 20)),
                FakeClientChatDetailsInteractor.SentMessage(null, fileIds(20 until 25)),
            ),
            interactor.sentMessages,
        )
        store.onDestroy()
    }

    @Test
    fun removesBatchFromPreviewRightAfterItIsSent() = runStoreTest {
        val allowSecondBatch = CompletableDeferred<Unit>()
        val interactor = FakeClientChatDetailsInteractor(
            upload = ::attachment,
            send = { message ->
                if (message.attachmentIds.first() == "file-10") allowSecondBatch.await()
            },
        )
        val store = createStore(interactor)
        val releasedFiles = mutableListOf<String>()

        store.handleEvent(Event.MessageTextChanged("text"))
        store.handleEvent(Event.FilesSelected(selectedFiles(12) { releasedFiles += it }))
        store.handleEvent(Event.MessageSent)
        advanceUntilIdle()

        assertTrue(store.currentState.isSending)
        assertEquals("", store.currentState.currentMessage)
        assertEquals(fileNames(10 until 12), store.currentState.selectedFiles.map { it.file.fileName })
        assertEquals(fileNames(0 until 10), releasedFiles)

        allowSecondBatch.complete(Unit)
        advanceUntilIdle()

        assertFalse(store.currentState.isSending)
        assertTrue(store.currentState.selectedFiles.isEmpty())
        assertEquals(fileNames(0 until 12), releasedFiles)
        store.onDestroy()
    }

    @Test
    fun keepsUnsentBatchesWhenSendingFails() = runStoreTest {
        var failSecondBatch = true
        val interactor = FakeClientChatDetailsInteractor(
            upload = ::attachment,
            send = { message ->
                if (failSecondBatch && message.attachmentIds.first() == "file-10") error("network")
            },
        )
        val store = createStore(interactor)

        store.handleEvent(Event.MessageTextChanged("text"))
        store.handleEvent(Event.FilesSelected(selectedFiles(15)))
        store.handleEvent(Event.MessageSent)
        advanceUntilIdle()

        assertFalse(store.currentState.isSending)
        assertEquals("", store.currentState.currentMessage)
        assertEquals(fileNames(10 until 15), store.currentState.selectedFiles.map { it.file.fileName })
        assertIs<SideEffect.Error>(store.sideEffect.first())

        failSecondBatch = false
        store.handleEvent(Event.MessageSent)
        advanceUntilIdle()

        assertEquals(
            listOf(
                FakeClientChatDetailsInteractor.SentMessage("text", fileIds(0 until 10)),
                FakeClientChatDetailsInteractor.SentMessage(null, fileIds(10 until 15)),
            ),
            interactor.sentMessages,
        )
        assertEquals(fileNames(0 until 15), interactor.uploadedFileNames)
        assertTrue(store.currentState.selectedFiles.isEmpty())
        store.onDestroy()
    }

    @Test
    fun sendsTextWithoutFiles() = runStoreTest {
        val interactor = FakeClientChatDetailsInteractor(upload = ::attachment)
        val store = createStore(interactor)

        store.handleEvent(Event.MessageTextChanged("text"))
        store.handleEvent(Event.MessageSent)
        advanceUntilIdle()

        assertEquals(listOf(FakeClientChatDetailsInteractor.SentMessage("text", emptyList())), interactor.sentMessages)
        assertEquals("", store.currentState.currentMessage)
        store.onDestroy()
    }

    @Test
    fun ignoresFilesSelectedWhileSending() = runStoreTest {
        val allowSend = CompletableDeferred<Unit>()
        val interactor = FakeClientChatDetailsInteractor(upload = ::attachment, send = { allowSend.await() })
        val store = createStore(interactor)
        var lateFileReleaseCount = 0

        store.handleEvent(Event.FilesSelected(selectedFiles(1)))
        store.handleEvent(Event.MessageSent)
        runCurrent()
        store.handleEvent(Event.FilesSelected(listOf(selectedFile("late.pdf") { lateFileReleaseCount++ })))

        assertEquals(fileNames(0 until 1), store.currentState.selectedFiles.map { it.file.fileName })
        assertEquals(1, lateFileReleaseCount)

        allowSend.complete(Unit)
        advanceUntilIdle()

        assertEquals(listOf(fileIds(0 until 1)), interactor.sentMessages.map { it.attachmentIds })
        assertTrue(store.currentState.selectedFiles.isEmpty())
        store.onDestroy()
    }

    private fun runStoreTest(block: suspend TestScope.() -> Unit) = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        try {
            block()
        } finally {
            Dispatchers.resetMain()
        }
    }

    private fun TestScope.createStore(interactor: FakeClientChatDetailsInteractor): ClientChatDetailsStore {
        val store = ClientChatDetailsStore(
            chatDetailsInteractor = interactor,
            userInteractor = FakeUserInteractor,
            errorHandler = FakeErrorHandler,
            fileOpener = FakeFileOpener(),
            sessionId = "",
            shouldReconnect = false,
        )
        runCurrent()
        return store
    }

    private fun selectedFiles(
        count: Int,
        onRelease: (fileName: String) -> Unit = {},
    ): List<SelectedFile> {
        return fileNames(0 until count).map { fileName ->
            selectedFile(fileName) { onRelease(fileName) }
        }
    }

    private fun selectedFile(fileName: String, onRelease: () -> Unit = {}): SelectedFile {
        return SelectedFile(
            fileName = fileName,
            contentType = "application/pdf",
            size = 1,
            previewUri = "file:///$fileName",
            sourceProvider = { Buffer() },
            releaseAccess = onRelease,
        )
    }

    private fun fileNames(indices: IntRange): List<String> = indices.map { "file-$it.pdf" }

    private fun fileIds(indices: IntRange): List<String> = indices.map { "file-$it" }

    private fun attachment(fileName: String): CommonAttachment {
        return CommonAttachment(
            fileId = fileName.removeSuffix(".pdf"),
            originalName = fileName,
            contentType = "application/pdf",
            size = 1,
            downloadUrl = "",
        )
    }
}
