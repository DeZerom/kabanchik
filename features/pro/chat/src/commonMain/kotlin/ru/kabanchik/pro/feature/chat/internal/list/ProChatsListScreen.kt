package ru.kabanchik.pro.feature.chat.internal.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import kabanchik.features.pro.chat.generated.resources.Res
import kabanchik.features.pro.chat.generated.resources.chat_list_request_client
import kabanchik.features.pro.chat.generated.resources.chat_list_request_client_message
import kabanchik.features.pro.chat.generated.resources.chat_list_title
import org.jetbrains.compose.resources.stringResource
import ru.kabanchik.common.features.chat.logic.list.CommonChatListContent
import ru.kabanchik.common.scaffold.toolbar.CommonToolbarModel
import ru.kabanchik.common.screenSize.PartFillingScreen
import ru.kabanchik.common.tools.extensions.asTextResource
import ru.kabanchik.common.uiKit.widgets.CommonScreenLoader
import ru.kabanchik.common.uiKit.widgets.toolbar.AffectScaffold
import ru.kabanchik.pro.feature.chat.api.list.ProChatsListComponent

@Composable
internal fun ProChatsListScreen(
    component: ProChatsListComponent
) {
    val state by component.state.collectAsState()

    AffectScaffold(
        toolbar = CommonToolbarModel.Title(Res.string.chat_list_title.asTextResource())
    )

    PartFillingScreen {
        if (state.isLoading) {
            CommonScreenLoader()
        } else {
            CommonChatListContent(
                items = state.chats,
                emptyListMessage = stringResource(Res.string.chat_list_request_client_message),
                emptyListButtonText = stringResource(Res.string.chat_list_request_client),
                emptyListButtonLoading = state.isWaitingForClient,
                onItemClick = component::onChatClicked,
                onAddClick = component::onRequestClientClicked,
                listFabContentDescription = stringResource(Res.string.chat_list_request_client),
                listFabLoading = state.isWaitingForClient
            )
        }
    }
}
