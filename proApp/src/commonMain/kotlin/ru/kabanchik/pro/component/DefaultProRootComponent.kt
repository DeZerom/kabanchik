package ru.kabanchik.pro.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.value.Value
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import ru.kabanchik.common.snackBar.api.SnackBarComponent
import ru.kabanchik.common.snackBar.api.SnackBarData.Error
import ru.kabanchik.pro.feature.auth.api.ProAuthComponent
import ru.kabanchik.pro.feature.auth.api.ProAuthDependencies.Factory
import ru.kabanchik.pro.feature.chatDetails.api.ProChatDetailsComponent
import ru.kabanchik.pro.feature.chatDetails.api.ProChatDetailsDependencies

class DefaultProRootComponent(
    componentContext: ComponentContext
) : ProRootComponent, ComponentContext by componentContext, KoinComponent {
    private val stackNavigation = StackNavigation<Config>()
    override val stack: Value<ChildStack<*, ProRootComponent.Child>> = childStack(
        source = stackNavigation,
        serializer = Config.serializer(),
        initialStack = { listOf(Config.Auth) },
        childFactory = ::createChild
    )

    override val snackBarComponent: SnackBarComponent = SnackBarComponent.create(
        componentContext = childContext("pro_snack_bar")
    )

    private fun createChild(config: Config, componentContext: ComponentContext): ProRootComponent.Child {
        return when (config) {
            Config.Auth -> {
                ProRootComponent.Child.Auth(
                    component = ProAuthComponent.create(
                        componentContext = componentContext,
                        dependencies = Factory(
                            authInteractor = get(),
                            userInteractor = get()
                        ),
                        onAuthorize = { stackNavigation.pushNew(Config.ChatDetails) },
                        onError = { snackBarComponent.setData(Error(it)) },
                    )
                )
            }
            Config.ChatDetails -> {
                ProRootComponent.Child.ChatDetails(
                    component = ProChatDetailsComponent.create(
                        componentContext = componentContext,
                        dependencies = ProChatDetailsDependencies.Factory(
                            chatDetailsInteractor = get(),
                            userInteractor = get()
                        )
                    )
                )
            }
        }
    }

    @Serializable
    sealed class Config {
        @Serializable
        data object Auth : Config()

        @Serializable
        data object ChatDetails : Config()
    }
}