package ru.kabanchik.client.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.router.stack.replaceCurrent
import com.arkivanov.decompose.value.Value
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import ru.kabanchik.client.feature.auth.api.flow.AuthFlowComponent
import ru.kabanchik.client.feature.auth.api.flow.AuthFlowDependencies
import ru.kabanchik.client.feature.splash.api.ClientSplashComponent
import ru.kabanchik.client.feature.splash.api.ClientSplashDependencies
import ru.kabanchik.common.snackBar.api.SnackBarComponent
import ru.kabanchik.feature.client.chatDetails.api.flow.ClientChatFlowComponent
import ru.kabanchik.feature.client.chatDetails.api.flow.ClientChatFlowDependencies

class DefaultRootComponent(
    componentContext: ComponentContext
) : RootComponent, ComponentContext by componentContext, KoinComponent {
    private val stackNavigation = StackNavigation<Config>()
    override val stack: Value<ChildStack<*, RootComponent.Child>> = childStack(
        source = stackNavigation,
        serializer = Config.serializer(),
        initialStack = { listOf(Config.Splash) },
        childFactory = ::createChild,
    )

    override val snackBarComponent: SnackBarComponent = SnackBarComponent.create(
        componentContext = childContext("root_snack_bar_component")
    )

    private fun createChild(config: Config, context: ComponentContext): RootComponent.Child {
        return when (config) {
            Config.Splash -> {
                RootComponent.Child.Splash(
                    component = ClientSplashComponent.create(
                        componentContext = context,
                        dependencies = ClientSplashDependencies.Factory(
                            splashInteractor = get()
                        ),
                        navigateChatsList = { stackNavigation.pushNew(Config.Chat) },
                        navigateAuth = { stackNavigation.pushNew(Config.Auth) }
                    )
                )
            }
            Config.Auth -> {
                RootComponent.Child.Auth(
                    component = AuthFlowComponent.create(
                        componentContext = context,
                        dependencies = AuthFlowDependencies.Factory(
                            authInteractor = get(),
                            errorHandler = get()
                        ),
                        showSnackBar = { snackBarComponent.setData(it) },
                        navigateHome = { stackNavigation.replaceCurrent(Config.Chat) }
                    )
                )
            }
            Config.Chat -> {
                RootComponent.Child.Chat(
                    component = ClientChatFlowComponent.create(
                        componentContext = context,
                        showSnackBar = { snackBarComponent.setData(it) },
                        dependencies = ClientChatFlowDependencies.Factory(
                            listInteractor = get(),
                            chatDetailsInteractor = get(),
                            userInteractor = get(),
                            errorHandler = get(),
                            filePicker = get(),
                            fileOpener = get(),
                        )
                    )
                )
            }
        }
    }

    @Serializable
    private sealed class Config {
        @Serializable
        data object Splash : Config()
        @Serializable
        data object Auth : Config()
        @Serializable
        data object Chat : Config()
    }
}
