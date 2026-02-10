package ru.kabanchik.pro.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import ru.kabanchik.common.snackBar.api.SnackBarComponent
import ru.kabanchik.common.snackBar.api.SnackBarData
import ru.kabanchik.common.tools.textResource.TextResource
import ru.kabanchik.pro.feature.auth.api.ProAuthComponent
import ru.kabanchik.pro.feature.auth.api.ProAuthDependencies

class DefaultRootComponent(
    componentContext: ComponentContext
) : RootComponent, ComponentContext by componentContext, KoinComponent {
    private val stackNavigation = StackNavigation<Config>()
    override val stack: Value<ChildStack<*, RootComponent.Child>> = childStack(
        source = stackNavigation,
        serializer = Config.serializer(),
        initialStack = { listOf(Config.Auth) },
        childFactory = ::createChild
    )

    override val snackBarComponent: SnackBarComponent = SnackBarComponent.create(
        componentContext = childContext("pro_snack_bar")
    )

    private fun createChild(config: Config, componentContext: ComponentContext): RootComponent.Child {
        return when (config) {
            Config.Auth -> {
                RootComponent.Child.Auth(
                    component = ProAuthComponent.create(
                        componentContext = componentContext,
                        dependencies = ProAuthDependencies.Factory(
                            authInteractor = get()
                        ),
                        onAuthorize = { snackBarComponent.setData(SnackBarData.Success(text = TextResource.Raw("Bebebe"))) },
                        onError = { snackBarComponent.setData(SnackBarData.Error(it)) },
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