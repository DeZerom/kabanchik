package ru.kabanchik.pro.feature.chat.api.details

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.flow.StateFlow
import ru.kabanchik.pro.feature.chat.api.end.ProEndChatComponent

interface ProChatDetailsComponent {
    val state: StateFlow<ProChatDetailsContract.State>
    val endChatSlot: Value<ChildSlot<*, ProEndChatComponent>>

    fun onMessageChanged(newMessage: String)
    fun onFileSelectionRequested()
    fun onFileRemoved(fileId: String)
    fun onFileOpenRequested(fileId: String)
    fun onImageOpenRequested(imageUrl: String)
    fun onSendClicked()
    fun onEndClicked()
    fun onBackClicked()
}
