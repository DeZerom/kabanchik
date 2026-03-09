package ru.kabanchik.pro.feature.chatDetails.api.list

import kotlinx.coroutines.flow.StateFlow

interface ProChatsListComponent {
    val state: StateFlow<ProChatsListContract.State>

    fun onRequestClientClicked()
}