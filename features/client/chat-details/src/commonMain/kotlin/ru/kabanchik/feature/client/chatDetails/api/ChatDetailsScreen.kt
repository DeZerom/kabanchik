package ru.kabanchik.feature.client.chatDetails.api

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import ru.kabanchik.feature.client.chatDetails.internal.ChatDetailsContent

@Composable
fun ChatDetailsScreen(
    component: ChatDetailsComponent
) {
    val state by component.state.collectAsState()

    ChatDetailsContent(
        state = state,
        onLoginSelected = component::loginSelected,
        onMessageTextChanged = component::messageTextChanged,
        onMessageSent = component::messageSent
    )
}