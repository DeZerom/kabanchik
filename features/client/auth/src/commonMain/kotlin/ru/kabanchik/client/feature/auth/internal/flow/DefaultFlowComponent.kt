package ru.kabanchik.client.feature.auth.internal.flow

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.value.Value
import kotlinx.serialization.Serializable
import ru.kabanchik.client.feature.auth.api.flow.AuthFlowComponent
import ru.kabanchik.client.feature.auth.api.flow.AuthFlowDependencies
import ru.kabanchik.client.feature.auth.internal.auth.DefaultAuthComponent
import ru.kabanchik.client.feature.auth.internal.register.DefaultRegisterComponent
import ru.kabanchik.client.feature.auth.internal.register.RegisterDependencies

internal class DefaultFlowComponent(
    componentContext: ComponentContext,
    private val dependencies: AuthFlowDependencies
) : AuthFlowComponent, ComponentContext by componentContext {
    private val stackNavigation = StackNavigation<Config>()
    override val stack: Value<ChildStack<*, AuthFlowComponent.Child>> = childStack(
        source = stackNavigation,
        serializer = Config.serializer(),
        initialStack = { listOf(Config.Register) },
        childFactory = ::createChild
    )

    private fun createChild(config: Config, componentContext: ComponentContext): AuthFlowComponent.Child {
        return when (config) {
            Config.Auth -> {
                AuthFlowComponent.Child.Auth(
                    component = DefaultAuthComponent(componentContext)
                )
            }
            Config.Register -> {
                AuthFlowComponent.Child.Register(
                    component = DefaultRegisterComponent(
                        componentContext = componentContext,
                        dependencies = RegisterDependencies.Factory(dependencies = dependencies),
                        navigateBack = { stackNavigation.pop() }
                    )
                )
            }
        }
    }

    @Serializable
    private sealed class Config {
        @Serializable
        object Auth : Config()

        @Serializable
        object Register : Config()
    }
}