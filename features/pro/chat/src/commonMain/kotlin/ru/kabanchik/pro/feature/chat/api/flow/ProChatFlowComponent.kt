package ru.kabanchik.pro.feature.chat.api.flow

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import ru.kabanchik.common.feature.imageViewer.api.ImageViewerComponent
import ru.kabanchik.common.snackBar.api.SnackBarData

interface ProChatFlowComponent {
    val stack: Value<ChildStack<*, Child>>

    sealed interface Child {
        class List(val component: ru.kabanchik.pro.feature.chat.api.list.ProChatsListComponent) : Child
        class Details(val component: ru.kabanchik.pro.feature.chat.api.details.ProChatDetailsComponent) : Child
        class ImageViewer(val component: ImageViewerComponent) : Child
    }

    companion object {
        fun create(
            componentContext: ComponentContext,
            showSnackBar: (SnackBarData) -> Unit,
            dependencies: ru.kabanchik.pro.feature.chat.api.flow.ProChatFlowDependencies,
        ) : ProChatFlowComponent {
            return _root_ide_package_.ru.kabanchik.pro.feature.chat.internal.flow.DefaultProChatFlowComponent(
                componentContext = componentContext,
                showSnackBar = showSnackBar,
                dependencies = dependencies
            )
        }
    }
}
