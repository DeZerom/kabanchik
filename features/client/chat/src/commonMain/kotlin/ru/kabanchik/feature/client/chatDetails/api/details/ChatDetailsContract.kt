package ru.kabanchik.feature.client.chatDetails.api.details

import ru.kabanchik.common.feature.chat.model.CommonPendingFile
import ru.kabanchik.common.feature.chat.model.CommonUiMessage
import ru.kabanchik.common.filePicker.api.SelectedFile
import ru.kabanchik.common.tools.textResource.TextResource

class ChatDetailsContract {
    data class State(
        val login: String = "",
        val currentMessage: String = "",
        val selectedFiles: List<CommonPendingFile> = emptyList(),
        val messages: List<CommonUiMessage> = emptyList(),
        val isLoading: Boolean = false
    ) {
        val toolbarTitle = messages
            .filterIsInstance<CommonUiMessage.Message>()
            .firstOrNull()
            ?.text
            .orEmpty()

        val toolbarSubtitle = messages
            .filterIsInstance<CommonUiMessage.Message>()
            .firstOrNull { !it.isUserAuthor }
            ?.authorLogin
            .orEmpty()
    }

    sealed interface Event {
        class MessageTextChanged(val newText: String) : Event
        class FilesSelected(val files: List<SelectedFile>) : Event
        object MessageSent : Event
    }

    sealed interface SideEffect {
        class Error(val text: TextResource) : SideEffect
    }
}
