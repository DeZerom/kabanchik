package ru.kabanchik.pro.feature.chatDetails.api.list

class ProChatsListContract {
    data class State(
        val isLoading: Boolean = false,
        val isWaitingForClient: Boolean = false
    )

    sealed interface Event {
        object RequestChat : Event
    }

    sealed interface SideEffect
}