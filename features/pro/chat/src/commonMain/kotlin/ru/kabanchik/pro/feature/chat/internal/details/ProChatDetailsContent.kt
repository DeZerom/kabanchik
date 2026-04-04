package ru.kabanchik.pro.feature.chat.internal.details

import androidx.compose.runtime.Composable
import ru.kabanchik.common.features.chat.logic.CommonChatContent
import ru.kabanchik.common.uiKit.widgets.CommonScreenLoader
import ru.kabanchik.pro.feature.chat.api.details.ProChatDetailsContract

@Composable
internal fun ProChatDetailsContent(
    state: ProChatDetailsContract.State,
    onMessageTextChanged: (String) -> Unit,
    onMessageSent: () -> Unit,
) {
    if (state.isLoading) {
        CommonScreenLoader()
    } else {
        Chat(
            state = state,
            onMessageTextChanged = onMessageTextChanged,
            onMessageSent = onMessageSent
        )
    }
}

@Composable
private fun Chat(
    state: ProChatDetailsContract.State,
    onMessageTextChanged: (String) -> Unit,
    onMessageSent: () -> Unit
) {
    CommonChatContent(
        messages = state.messages,
        currentMessageText = state.currentMessage,
        onMessageTextChanged = onMessageTextChanged,
        onMessageSent = onMessageSent
    )
}