package ru.kabanchik.pro.feature.chat.internal.details

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.instancekeeper.retainedInstance
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.kabanchik.common.snackBar.api.SnackBarData
import ru.kabanchik.pro.feature.chat.api.details.ProChatDetailsComponent
import ru.kabanchik.pro.feature.chat.api.details.ProChatDetailsContract.Event
import ru.kabanchik.pro.feature.chat.api.details.ProChatDetailsContract.SideEffect
import ru.kabanchik.pro.feature.chat.api.details.ProChatDetailsContract.State
import ru.kabanchik.pro.feature.chat.api.details.ProChatDetailsDependencies

class DefaultProChatDetailsComponent(
    componentContext: ComponentContext,
    private val dependencies: ProChatDetailsDependencies,
    private val showSnackBar: (SnackBarData) -> Unit,
    private val navigateBack: () -> Unit,
    private val sessionId: String,
    private val shouldReconnect: Boolean
) : ProChatDetailsComponent, ComponentContext by componentContext {
    private val store = retainedInstance {
        ProChatDetailsStore(
            chatDetailsInteractor = dependencies.chatDetailsInteractor,
            userInteractor = dependencies.userInteractor,
            errorHandler = dependencies.errorHandler,
            fileOpener = dependencies.fileOpener,
            sessionId = sessionId,
            shouldReconnect = shouldReconnect
        )
    }
    override val state: StateFlow<State> = store.state

    private val coroutineScope = coroutineScope()
    private var isFilePickerOpen = false

    init {
        observeSideEffects()
    }

    override fun onMessageChanged(newMessage: String) {
        store.handleEvent(Event.MessageTextChanged(newMessage))
    }

    override fun onFileSelectionRequested() {
        if (isFilePickerOpen) return

        isFilePickerOpen = true
        coroutineScope.launch {
            try {
                val files = dependencies.filePicker.pickFiles()
                if (files.isNotEmpty()) {
                    store.handleEvent(Event.FilesSelected(files))
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

    override fun onFileRemoved(fileId: String) {
        store.handleEvent(Event.FileRemoved(fileId))
    }

    override fun onFileOpenRequested(fileId: String) {
        store.handleEvent(Event.FileOpenRequested(fileId))
    }

    override fun onSendClicked() {
        store.handleEvent(Event.MessageSent)
    }

    override fun onBackClicked() {
        navigateBack()
    }

    private fun observeSideEffects() {
        store.sideEffect.onEach { effect ->
            when (effect) {
                is SideEffect.Error -> {
                    showSnackBar(SnackBarData.Error(effect.text))
                }
            }
        }.launchIn(coroutineScope)
    }
}
