package ru.kabanchik.feature.client.chatDetails.internal.details

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.instancekeeper.retainedInstance
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.kabanchik.common.features.chat.logic.details.imageUrls
import ru.kabanchik.common.snackBar.api.SnackBarData
import ru.kabanchik.feature.client.chatDetails.api.details.ChatDetailsContract
import ru.kabanchik.feature.client.chatDetails.api.details.ClientChatDetailsComponent
import ru.kabanchik.feature.client.chatDetails.api.details.ClientChatDetailsDependencies

internal class DefaultClientChatDetailsComponent(
    componentContext: ComponentContext,
    private val dependencies: ClientChatDetailsDependencies,
    private val showSnackBar: (SnackBarData) -> Unit,
    private val navigateBack: () -> Unit,
    private val navigateImageViewer: (List<String>, String) -> Unit,
    private val sessionId: String,
    private val shouldReconnect: Boolean
) : ClientChatDetailsComponent, ComponentContext by componentContext {
    private val store = retainedInstance {
        ClientChatDetailsStore(
            chatDetailsInteractor = dependencies.chatDetailsInteractor,
            userInteractor = dependencies.userInteractor,
            errorHandler = dependencies.errorHandler,
            fileOpener = dependencies.fileOpener,
            sessionId = sessionId,
            shouldReconnect = shouldReconnect
        )
    }
    override val state: StateFlow<ChatDetailsContract.State> = store.state

    private val coroutineScope = coroutineScope()
    private var isFilePickerOpen = false

    init {
        observeSideEffects()
    }

    override fun messageTextChanged(newText: String) {
        store.handleEvent(ChatDetailsContract.Event.MessageTextChanged(newText))
    }

    override fun filesSelectionRequested() {
        if (isFilePickerOpen) return

        isFilePickerOpen = true
        coroutineScope.launch {
            try {
                val files = dependencies.filePicker.pickFiles()
                if (files.isNotEmpty()) {
                    store.handleEvent(ChatDetailsContract.Event.FilesSelected(files))
                }
            } catch (error: Throwable) {
                if (error is CancellationException) throw error
                showSnackBar(
                    SnackBarData.Error(dependencies.errorHandler.handleError(error).defaultMessage)
                )
            } finally {
                isFilePickerOpen = false
            }
        }
    }

    override fun fileRemoved(fileId: String) {
        store.handleEvent(ChatDetailsContract.Event.FileRemoved(fileId))
    }

    override fun fileOpenRequested(fileId: String) {
        store.handleEvent(ChatDetailsContract.Event.FileOpenRequested(fileId))
    }

    override fun imageOpenRequested(imageUrl: String) {
        navigateImageViewer(state.value.messages.imageUrls(), imageUrl)
    }

    override fun messageSent() {
        store.handleEvent(ChatDetailsContract.Event.MessageSent)
    }

    override fun onBackClicked() {
        navigateBack()
    }

    private fun observeSideEffects() {
        store.sideEffect.onEach { effect ->
            when (effect) {
                is ChatDetailsContract.SideEffect.Error -> {
                    showSnackBar(SnackBarData.Error(effect.text))
                }
            }
        }.launchIn(coroutineScope)
    }
}
