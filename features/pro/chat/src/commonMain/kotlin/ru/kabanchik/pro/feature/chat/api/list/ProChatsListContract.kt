package ru.kabanchik.pro.feature.chat.api.list

import ru.kabanchik.common.tools.textResource.TextResource

class ProChatsListContract {
    data class State(
        val isLoading: Boolean = false,
        val isWaitingForClient: Boolean = false
    )

    sealed interface Event {
        object RequestChat : Event
    }

    sealed interface SideEffect {
        class ShowError(val message: TextResource) : SideEffect
        object NavigateDetails : SideEffect
    }
}