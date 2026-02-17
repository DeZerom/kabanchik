package ru.kabanchik.pro.feature.chatDetails.api

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import ru.kabanchik.common.screenSize.PartFillingScreen
import ru.kabanchik.pro.feature.chatDetails.internal.ProChatDetailsContent

@Composable
fun ProChatDetailsScreen(component: ProChatDetailsComponent) {
    val state by component.state.collectAsState()

    PartFillingScreen {
        ProChatDetailsContent(
            state = state,
            onMessageTextChanged = component::onMessageChanged,
            onMessageSent = component::onSendClicked
        )
    }
}