package ru.kabanchik.client.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import ru.kabanchik.client.feature.auth.api.flow.AuthFlowComponent
import ru.kabanchik.client.feature.auth.api.flow.AuthFlowDependencies
import ru.kabanchik.feature.client.chatDetails.api.ChatDetailsComponent
import ru.kabanchik.feature.client.chatDetails.api.ChatDetailsDependencies

class DefaultRootComponent(
    componentContext: ComponentContext
) : RootComponent, ComponentContext by componentContext, KoinComponent {
    private val stackNavigation = StackNavigation<Config>()
    override val stack: Value<ChildStack<*, RootComponent.Child>> = childStack(
        source = stackNavigation,
        serializer = Config.serializer(),
        initialStack = { listOf(Config.Auth) },
        childFactory = ::createChild,
    )

    private fun createChild(config: Config, context: ComponentContext): RootComponent.Child {
        return when (config) {
            Config.Auth -> {
                RootComponent.Child.Auth(
                    component = AuthFlowComponent.create(
                        componentContext = context,
                        dependencies = AuthFlowDependencies.Factory(
                            authInteractor = get()
                        )
                    )
                )
            }
            Config.Chat -> {
                RootComponent.Child.Chat(
                    component = ChatDetailsComponent.create(
                        componentContext = context,
                        dependencies = ChatDetailsDependencies.Factory(
                            chatDetailsInteractor = get()
                        )
                    )
                )
            }
        }
    }

    @Serializable
    private sealed class Config {
        @Serializable
        data object Auth : Config()
        @Serializable
        data object Chat : Config()
    }
}