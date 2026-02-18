package ru.kabanchik.feature.client.chatDetails.api

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import ru.kabanchik.common.screenSize.PartFillingScreen
import ru.kabanchik.feature.client.chatDetails.internal.ChatDetailsContent

@Composable
fun ChatDetailsScreen(
    component: ChatDetailsComponent
) {
    val state by component.state.collectAsState()

    PartFillingScreen {
        ChatDetailsContent(
            state = state,
            onMessageTextChanged = component::messageTextChanged,
            onMessageSent = component::messageSent
        )
    }
}