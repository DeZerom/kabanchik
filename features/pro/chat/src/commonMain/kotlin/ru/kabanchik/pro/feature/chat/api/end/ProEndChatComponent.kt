package ru.kabanchik.pro.feature.chat.api.end

import kotlinx.coroutines.flow.StateFlow
import ru.kabanchik.common.chat.model.CommonChatResolution

/**
 * Шит завершения заявки оператором.
 */
interface ProEndChatComponent {
    val state: StateFlow<ProEndChatContract.State>

    fun onResolutionSelected(resolution: CommonChatResolution)
    fun onEndClicked()
    fun onDismissRequested()
}
