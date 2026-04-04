package ru.kabanchik.feature.client.chatDetails.api.flow

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.stack.Children
import ru.kabanchik.feature.client.chatDetails.internal.details.ClientChatDetailsScreen
import ru.kabanchik.feature.client.chatDetails.internal.list.ClientChatsListScreen

@Composable
fun ClientChatFlowScreen(
    component: ClientChatFlowComponent
) {
    Children(
        stack = component.stackNavigation,
    ) { child ->
        when (val instance = child.instance) {
            is ClientChatFlowComponent.Child.Details -> {
                ClientChatDetailsScreen(instance.component)
            }
            is ClientChatFlowComponent.Child.List -> {
                ClientChatsListScreen(instance.component)
            }
        }
    }
}