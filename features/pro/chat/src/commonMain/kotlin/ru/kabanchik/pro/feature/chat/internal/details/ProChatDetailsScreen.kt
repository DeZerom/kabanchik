package ru.kabanchik.pro.feature.chat.internal.details

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import ru.kabanchik.common.screenSize.PartFillingScreen
import ru.kabanchik.common.uiKit.widgets.toolbar.AffectScaffold

@Composable
internal fun ProChatDetailsScreen(component: ru.kabanchik.pro.feature.chat.api.details.ProChatDetailsComponent) {
    val state by component.state.collectAsState()

    AffectScaffold()

    PartFillingScreen {
        ProChatDetailsContent(
            state = state,
            onMessageTextChanged = component::onMessageChanged,
            onMessageSent = component::onSendClicked
        )
    }
}