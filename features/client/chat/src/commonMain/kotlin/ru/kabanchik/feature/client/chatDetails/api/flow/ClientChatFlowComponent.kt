package ru.kabanchik.feature.client.chatDetails.api.flow

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import ru.kabanchik.common.snackBar.api.SnackBarData
import ru.kabanchik.feature.client.chatDetails.api.details.ClientChatDetailsComponent
import ru.kabanchik.feature.client.chatDetails.api.list.ClientChatsListComponent
import ru.kabanchik.feature.client.chatDetails.internal.flow.DefaultClientChatFlowComponent

interface ClientChatFlowComponent {
    val stackNavigation: Value<ChildStack<*, Child>>
    
    sealed interface Child {
        class List(val component: ClientChatsListComponent) : Child
        class Details(val component: ClientChatDetailsComponent) : Child
    }

    companion object {
        fun create(
            componentContext: ComponentContext,
            dependencies: ClientChatFlowDependencies,
            showSnackBar: (SnackBarData) -> Unit
        ): ClientChatFlowComponent {
            return DefaultClientChatFlowComponent(
                componentContext = componentContext,
                dependencies = dependencies,
                showSnackBar = showSnackBar
            )
        }
    }
}