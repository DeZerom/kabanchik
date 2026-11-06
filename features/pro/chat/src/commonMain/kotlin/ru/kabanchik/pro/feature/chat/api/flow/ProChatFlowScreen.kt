package ru.kabanchik.pro.feature.chat.api.flow

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.stack.Children
import ru.kabanchik.common.feature.imageViewer.api.ImageViewerScreen
import ru.kabanchik.pro.feature.chat.api.flow.ProChatFlowComponent.Child
import ru.kabanchik.pro.feature.chat.internal.details.ProChatDetailsScreen
import ru.kabanchik.pro.feature.chat.internal.list.ProChatsListScreen

@Composable
fun ProChatFlowScreen(
    component: ru.kabanchik.pro.feature.chat.api.flow.ProChatFlowComponent
) {
    Children(
        stack = component.stack
    ) {
        when (val child = it.instance) {
            is Child.List -> ProChatsListScreen(child.component)
            is Child.Details -> ProChatDetailsScreen(child.component)
            is Child.ImageViewer -> ImageViewerScreen(child.component)
        }
    }
}
