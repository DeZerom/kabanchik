package ru.kabanchik.pro.feature.chat.api.list

import kotlinx.coroutines.flow.StateFlow

interface ProChatsListComponent {
    val state: StateFlow<ProChatsListContract.State>

    fun onRequestClientClicked()
    fun onChatClicked(id: String)
}