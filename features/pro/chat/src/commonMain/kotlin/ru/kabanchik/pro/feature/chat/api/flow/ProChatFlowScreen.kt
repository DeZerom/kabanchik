package ru.kabanchik.pro.feature.chat.api.flow

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.stack.Children

@Composable
fun ProChatFlowScreen(
    component: ru.kabanchik.pro.feature.chat.api.flow.ProChatFlowComponent
) {
    Children(
        stack = component.stack
    ) {
        when (val child = it.instance) {
            is ru.kabanchik.pro.feature.chat.api.flow.ProChatFlowComponent.Child.List -> _root_ide_package_.ru.kabanchik.pro.feature.chat.internal.list.ProChatsListScreen(
                child.component
            )
            is ru.kabanchik.pro.feature.chat.api.flow.ProChatFlowComponent.Child.Details -> _root_ide_package_.ru.kabanchik.pro.feature.chat.internal.details.ProChatDetailsScreen(
                child.component
            )
        }
    }
}