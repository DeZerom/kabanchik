package ru.kabanchik.pro.feature.chatDetails.api.flow

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import ru.kabanchik.common.snackBar.api.SnackBarData
import ru.kabanchik.pro.feature.chatDetails.api.details.ProChatDetailsComponent
import ru.kabanchik.pro.feature.chatDetails.api.list.ProChatsListComponent
import ru.kabanchik.pro.feature.chatDetails.internal.flow.DefaultProChatFlowComponent

interface ProChatFlowComponent {
    val stack: Value<ChildStack<*, Child>>

    sealed interface Child {
        class List(val component: ProChatsListComponent) : Child
        class Details(val component: ProChatDetailsComponent) : Child
    }

    companion object {
        fun create(
            componentContext: ComponentContext,
            showSnackBar: (SnackBarData) -> Unit,
            dependencies: ProChatFlowDependencies,
        ) : ProChatFlowComponent {
            return DefaultProChatFlowComponent(
                componentContext = componentContext,
                showSnackBar = showSnackBar,
                dependencies = dependencies
            )
        }
    }
}