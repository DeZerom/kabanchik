package ru.kabanchik.pro.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import kotlinx.serialization.Serializable
import ru.kabanchik.common.snackBar.api.SnackBarComponent
import ru.kabanchik.pro.feature.auth.api.flow.ProAuthFlowComponent
import ru.kabanchik.pro.feature.auth.api.flow.ProAuthFlowDependencies

class DefaultRootComponent(
    componentContext: ComponentContext
) : RootComponent, ComponentContext by componentContext {
    private val stackNavigation = StackNavigation<Config>()
    override val stack: Value<ChildStack<*, RootComponent.Child>> = childStack(
        source = stackNavigation,
        serializer = Config.serializer(),
        initialStack = { listOf(Config.Auth) },
        childFactory = ::createChild
    )

    override val snackBarComponent: SnackBarComponent
        get() = TODO("Not yet implemented")

    private fun createChild(config: Config, componentContext: ComponentContext): RootComponent.Child {
        return when (config) {
            Config.Auth -> {
                RootComponent.Child.Auth(
                    component = ProAuthFlowComponent.create(
                        componentContext = componentContext,
                        dependencies = ProAuthFlowDependencies.Factory()
                    )
                )
            }
        }
    }

    @Serializable
    sealed class Config {
        @Serializable
        data object Auth : Config()
    }
}