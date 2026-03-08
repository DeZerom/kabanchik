package ru.kabanchik.feature.client.chatDetails.api.list

class ClientChatsListContract {
    data class State(
        val isLoading: Boolean = false,
        val isChatCreating: Boolean = false
    )

    sealed interface Event {
        object CreateChatClicked : Event
    }

    sealed interface SideEffect {
        object NavigateChatDetails : SideEffect
    }
}