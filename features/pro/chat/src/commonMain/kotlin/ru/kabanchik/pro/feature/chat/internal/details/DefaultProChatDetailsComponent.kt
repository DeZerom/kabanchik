package ru.kabanchik.pro.feature.chat.internal.details

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.slot.dismiss
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.instancekeeper.retainedInstance
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import kabanchik.features.pro.chat.generated.resources.Res
import kabanchik.features.pro.chat.generated.resources.chat_details_paused
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import ru.kabanchik.common.features.chat.logic.details.imageUrls
import ru.kabanchik.common.snackBar.api.SnackBarData
import ru.kabanchik.common.tools.extensions.asTextResource
import ru.kabanchik.pro.feature.chat.api.details.ProChatDetailsComponent
import ru.kabanchik.pro.feature.chat.api.details.ProChatDetailsContract.Event
import ru.kabanchik.pro.feature.chat.api.details.ProChatDetailsContract.SideEffect
import ru.kabanchik.pro.feature.chat.api.details.ProChatDetailsContract.State
import ru.kabanchik.pro.feature.chat.api.details.ProChatDetailsDependencies
import ru.kabanchik.pro.feature.chat.api.end.ProEndChatComponent
import ru.kabanchik.pro.feature.chat.internal.end.DefaultProEndChatComponent

class DefaultProChatDetailsComponent(
    componentContext: ComponentContext,
    private val dependencies: ProChatDetailsDependencies,
    private val showSnackBar: (SnackBarData) -> Unit,
    private val navigateBack: () -> Unit,
    private val navigateImageViewer: (List<String>, String) -> Unit,
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

    private val endChatNavigation = SlotNavigation<EndChatConfig>()
    override val endChatSlot: Value<ChildSlot<*, ProEndChatComponent>> = childSlot(
        source = endChatNavigation,
        serializer = EndChatConfig.serializer(),
        key = "EndChatSlot",
        // Назад обрабатывает сам bottom sheet: во время завершения его закрывать нельзя
        handleBackButton = false,
        childFactory = { _, childContext ->
            DefaultProEndChatComponent(
                componentContext = childContext,
                dependencies = dependencies,
                sessionId = sessionId,
                onFinished = { endChatNavigation.dismiss() },
                onError = { text ->
                    // Снекбар под открытым шитом не виден
                    endChatNavigation.dismiss()
                    showSnackBar(SnackBarData.Error(text))
                },
                onDismissed = { endChatNavigation.dismiss() },
            )
        }
    )

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

    override fun onImageOpenRequested(imageUrl: String) {
        navigateImageViewer(state.value.messages.imageUrls(), imageUrl)
    }

    override fun onSendClicked() {
        store.handleEvent(Event.MessageSent)
    }

    override fun onEndClicked() {
        if (state.value.canEnd) {
            endChatNavigation.activate(EndChatConfig)
        }
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
                SideEffect.ChatPaused -> {
                    showSnackBar(SnackBarData.Error(Res.string.chat_details_paused.asTextResource()))
                    navigateBack()
                }
            }
        }.launchIn(coroutineScope)
    }

    @Serializable
    private object EndChatConfig
}
