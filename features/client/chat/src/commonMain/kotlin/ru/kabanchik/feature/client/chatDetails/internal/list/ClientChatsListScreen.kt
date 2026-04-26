package ru.kabanchik.feature.client.chatDetails.internal.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kabanchik.features.client.chat.generated.resources.Res
import kabanchik.features.client.chat.generated.resources.chat_list_create_chat
import kabanchik.features.client.chat.generated.resources.chat_list_title
import org.jetbrains.compose.resources.stringResource
import ru.kabanchik.common.scaffold.toolbar.CommonToolbarModel
import ru.kabanchik.common.screenSize.PartFillingScreen
import ru.kabanchik.common.tools.textResource.TextResource
import ru.kabanchik.common.uiKit.widgets.CommonButton
import ru.kabanchik.common.uiKit.widgets.CommonScreenLoader
import ru.kabanchik.common.uiKit.widgets.toolbar.AffectScaffold
import ru.kabanchik.feature.client.chatDetails.api.list.ClientChatsListComponent

@Composable
internal fun ClientChatsListScreen(
    component: ClientChatsListComponent
) {
    val state by component.state.collectAsState()

    AffectScaffold(
        toolbar = CommonToolbarModel.Title(
            title = TextResource.Id(Res.string.chat_list_title)
        )
    )

    PartFillingScreen {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            if (state.isLoading) {
                CommonScreenLoader()
            } else {
                CommonButton(
                    onClick = component::onCreateChatClicked,
                    text = stringResource(Res.string.chat_list_create_chat),
                    isLoading = state.isChatCreating
                )
            }
        }
    }
}