package ru.kabanchik.feature.client.chatDetails.internal.details

import androidx.compose.runtime.Composable
import ru.kabanchik.common.features.chat.logic.details.CommonChatContent
import ru.kabanchik.common.uiKit.widgets.CommonScreenLoader
import ru.kabanchik.feature.client.chatDetails.api.details.ChatDetailsContract

@Composable
internal fun ChatDetailsContent(
    state: ChatDetailsContract.State,
    onMessageTextChanged: (String) -> Unit,
    onMessageSent: () -> Unit,
    onFileSelectionRequested: () -> Unit,
) {
    if (state.isLoading) {
        CommonScreenLoader()
    } else {
        Chat(
            state = state,
            onMessageTextChanged = onMessageTextChanged,
            onMessageSent = onMessageSent,
            onFileSelectionRequested = onFileSelectionRequested,
        )
    }
}

@Composable
private fun Chat(
    state: ChatDetailsContract.State,
    onMessageTextChanged: (String) -> Unit,
    onMessageSent: () -> Unit,
    onFileSelectionRequested: () -> Unit,
) {
    CommonChatContent(
        messages = state.messages,
        currentMessageText = state.currentMessage,
        onMessageTextChanged = onMessageTextChanged,
        onMessageSent = onMessageSent,
        onFileSelectionRequested = onFileSelectionRequested,
    )
}
