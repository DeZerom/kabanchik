package ru.kabanchik.feature.client.chatDetails.internal.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kabanchik.features.client.chat.generated.resources.Res
import kabanchik.features.client.chat.generated.resources.chat_list_create_chat
import org.jetbrains.compose.resources.stringResource
import ru.kabanchik.common.screenSize.PartFillingScreen
import ru.kabanchik.common.uiKit.widgets.CommonButton
import ru.kabanchik.feature.client.chatDetails.api.list.ClientChatsListComponent

@Composable
internal fun ClientChatsListScreen(
    component: ClientChatsListComponent
) {
    PartFillingScreen {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CommonButton(
                onClick = component::onCreateChatClicked,
                text = stringResource(Res.string.chat_list_create_chat)
            )
        }
    }
}