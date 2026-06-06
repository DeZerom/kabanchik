package ru.kabanchik.feature.client.chatDetails.internal.flow

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.value.Value
import kotlinx.serialization.Serializable
import ru.kabanchik.common.snackBar.api.SnackBarData
import ru.kabanchik.feature.client.chatDetails.api.details.ClientChatDetailsDependencies
import ru.kabanchik.feature.client.chatDetails.api.flow.ClientChatFlowComponent
import ru.kabanchik.feature.client.chatDetails.api.flow.ClientChatFlowDependencies
import ru.kabanchik.feature.client.chatDetails.api.list.ClientChatsListDependencies
import ru.kabanchik.feature.client.chatDetails.internal.details.DefaultClientChatDetailsComponent
import ru.kabanchik.feature.client.chatDetails.internal.list.DefaultClientChatsListComponent

internal class DefaultClientChatFlowComponent(
    componentContext: ComponentContext,
    private val dependencies: ClientChatFlowDependencies,
    private val showSnackBar: (SnackBarData) -> Unit
) : ClientChatFlowComponent, ComponentContext by componentContext {
    private val stack = StackNavigation<Config>()
    override val stackNavigation: Value<ChildStack<*, ClientChatFlowComponent.Child>> = childStack(
        source = stack,
        serializer = Config.serializer(),
        initialStack = { listOf(Config.List) },
        childFactory = ::createChild
    ) 
    
    private fun createChild(config: Config, componentContext: ComponentContext): ClientChatFlowComponent.Child {
        return when (config) {
            is Config.Details -> {
                ClientChatFlowComponent.Child.Details(
                    component = DefaultClientChatDetailsComponent(
                        componentContext = componentContext,
                        dependencies = ClientChatDetailsDependencies.Factory(dependencies),
                        showSnackBar = showSnackBar,
                        sessionId = config.sessionId
                    )
                )
            }
            Config.List -> {
                ClientChatFlowComponent.Child.List(
                    component = DefaultClientChatsListComponent(
                        componentContext = componentContext,
                        navigateChatDetails = { sessionId -> stack.pushNew(Config.Details(sessionId)) },
                        dependencies = ClientChatsListDependencies.Factory(dependencies),
                        showSnackBar = showSnackBar
                    )
                )
            }
        }
    }
    
    @Serializable
    private sealed class Config {
        @Serializable
        object List : Config()
        
        @Serializable
        data class Details(val sessionId: String) : Config()
    }
}
