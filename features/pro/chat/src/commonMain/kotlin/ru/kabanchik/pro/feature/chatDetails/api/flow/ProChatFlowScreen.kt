package ru.kabanchik.pro.feature.chatDetails.api.flow

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.stack.Children
import ru.kabanchik.pro.feature.chatDetails.internal.details.ProChatDetailsScreen
import ru.kabanchik.pro.feature.chatDetails.internal.list.ProChatsListScreen

@Composable
fun ProChatFlowScreen(
    component: ProChatFlowComponent
) {
    Children(
        stack = component.stack
    ) {
        when (val child = it.instance) {
            is ProChatFlowComponent.Child.List -> ProChatsListScreen(child.component)
            is ProChatFlowComponent.Child.Details -> ProChatDetailsScreen(child.component)
        }
    }
}