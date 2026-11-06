package ru.kabanchik.pro.feature.chat.internal.details

import androidx.compose.runtime.Composable
import ru.kabanchik.common.features.chat.logic.details.CommonChatContent
import ru.kabanchik.common.uiKit.widgets.CommonScreenLoader
import ru.kabanchik.pro.feature.chat.api.details.ProChatDetailsContract

@Composable
internal fun ProChatDetailsContent(
    state: ProChatDetailsContract.State,
    onMessageTextChanged: (String) -> Unit,
    onMessageSent: () -> Unit,
    onFileSelectionRequested: () -> Unit,
    onFileRemoved: (String) -> Unit,
    onImageClicked: (String) -> Unit,
    onFileClicked: (String) -> Unit,
) {
    if (state.isLoading) {
        CommonScreenLoader()
    } else {
        Chat(
            state = state,
            onMessageTextChanged = onMessageTextChanged,
            onMessageSent = onMessageSent,
            onFileSelectionRequested = onFileSelectionRequested,
            onFileRemoved = onFileRemoved,
            onImageClicked = onImageClicked,
            onFileClicked = onFileClicked,
        )
    }
}

@Composable
private fun Chat(
    state: ProChatDetailsContract.State,
    onMessageTextChanged: (String) -> Unit,
    onMessageSent: () -> Unit,
    onFileSelectionRequested: () -> Unit,
    onFileRemoved: (String) -> Unit,
    onImageClicked: (String) -> Unit,
    onFileClicked: (String) -> Unit,
) {
    CommonChatContent(
        messages = state.messages,
        currentMessageText = state.currentMessage,
        selectedFiles = state.selectedFiles,
        isSending = state.isSending,
        onMessageTextChanged = onMessageTextChanged,
        onMessageSent = onMessageSent,
        onFileSelectionRequested = onFileSelectionRequested,
        onFileRemoved = { onFileRemoved(it.id) },
        onImageClicked = onImageClicked,
        onFileClicked = onFileClicked,
    )
}
