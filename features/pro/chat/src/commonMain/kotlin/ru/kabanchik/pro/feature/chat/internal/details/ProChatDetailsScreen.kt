package ru.kabanchik.pro.feature.chat.internal.details

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import kabanchik.features.pro.chat.generated.resources.Res
import kabanchik.features.pro.chat.generated.resources.chat_details_end
import ru.kabanchik.common.scaffold.toolbar.CommonToolbarAction
import ru.kabanchik.common.scaffold.toolbar.CommonToolbarModel
import ru.kabanchik.common.screenSize.PartFillingScreen
import ru.kabanchik.common.tools.extensions.asTextResource
import ru.kabanchik.common.tools.textResource.TextResource
import ru.kabanchik.common.uiKit.widgets.toolbar.AffectScaffold
import ru.kabanchik.pro.feature.chat.api.details.ProChatDetailsComponent
import ru.kabanchik.pro.feature.chat.internal.end.ProEndChatSheet

@Composable
internal fun ProChatDetailsScreen(component: ProChatDetailsComponent) {
    val state by component.state.collectAsState()
    val endChatSlot by component.endChatSlot.subscribeAsState()

    AffectScaffold(
        toolbar = CommonToolbarModel.BackButtonTitleSubtitle(
            title = TextResource.Raw(state.toolbarTitle),
            subtitle = TextResource.Raw(state.toolbarSubtitle),
            onBackClicked = component::onBackClicked,
            action = CommonToolbarAction(
                text = Res.string.chat_details_end.asTextResource(),
                onClick = component::onEndClicked,
            ).takeIf { state.canEnd }
        )
    )

    PartFillingScreen {
        ProChatDetailsContent(
            state = state,
            onMessageTextChanged = component::onMessageChanged,
            onMessageSent = component::onSendClicked,
            onFileSelectionRequested = component::onFileSelectionRequested,
            onFileRemoved = component::onFileRemoved,
            onImageClicked = component::onImageOpenRequested,
            onFileClicked = component::onFileOpenRequested,
        )
    }

    endChatSlot.child?.instance?.let { endChatComponent ->
        ProEndChatSheet(endChatComponent)
    }
}
