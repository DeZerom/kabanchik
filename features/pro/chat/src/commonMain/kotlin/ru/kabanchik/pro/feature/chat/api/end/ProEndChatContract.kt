package ru.kabanchik.pro.feature.chat.api.end

import ru.kabanchik.common.chat.model.CommonChatResolution
import ru.kabanchik.common.tools.textResource.TextResource

class ProEndChatContract {
    data class State(
        val resolutions: List<CommonChatResolution> = CommonChatResolution.entries,
        val selectedResolution: CommonChatResolution = CommonChatResolution.Closed,
        val isEnding: Boolean = false,
    )

    sealed interface Event {
        class ResolutionSelected(val resolution: CommonChatResolution) : Event
        object EndClicked : Event
    }

    sealed interface SideEffect {
        object Finished : SideEffect
        class Error(val text: TextResource) : SideEffect
    }
}
