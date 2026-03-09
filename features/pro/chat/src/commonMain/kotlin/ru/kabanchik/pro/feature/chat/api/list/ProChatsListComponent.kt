package ru.kabanchik.pro.feature.chat.api.list

import kotlinx.coroutines.flow.StateFlow

interface ProChatsListComponent {
    val state: StateFlow<ru.kabanchik.pro.feature.chat.api.list.ProChatsListContract.State>

    fun onRequestClientClicked()
}