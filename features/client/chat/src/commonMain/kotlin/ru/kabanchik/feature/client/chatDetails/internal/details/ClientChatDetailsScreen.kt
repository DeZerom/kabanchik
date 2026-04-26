package ru.kabanchik.feature.client.chatDetails.internal.details

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import ru.kabanchik.common.screenSize.PartFillingScreen
import ru.kabanchik.common.uiKit.widgets.toolbar.AffectScaffold
import ru.kabanchik.feature.client.chatDetails.api.details.ClientChatDetailsComponent

@Composable
internal fun ClientChatDetailsScreen(
    component: ClientChatDetailsComponent
) {
    val state by component.state.collectAsState()

    AffectScaffold()

    PartFillingScreen {
        ChatDetailsContent(
            state = state,
            onMessageTextChanged = component::messageTextChanged,
            onMessageSent = component::messageSent
        )
    }
}