package ru.kabanchik.pro.feature.chat.internal.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kabanchik.features.pro.chat.generated.resources.Res
import kabanchik.features.pro.chat.generated.resources.chat_list_request_client
import org.jetbrains.compose.resources.stringResource
import ru.kabanchik.common.screenSize.PartFillingScreen
import ru.kabanchik.common.uiKit.widgets.CommonButton
import ru.kabanchik.common.uiKit.widgets.CommonScreenLoader
import ru.kabanchik.pro.feature.chat.api.list.ProChatsListComponent

@Composable
internal fun ProChatsListScreen(
    component: ProChatsListComponent
) {
   val state by component.state.collectAsState()

    PartFillingScreen {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (state.isLoading) {
                CommonScreenLoader()
            } else {
                CommonButton(
                    onClick = component::onRequestClientClicked,
                    text = stringResource(Res.string.chat_list_request_client),
                    isLoading = state.isWaitingForClient
                )
            }
        }
    }
}