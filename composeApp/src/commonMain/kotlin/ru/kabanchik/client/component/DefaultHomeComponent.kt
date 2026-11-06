package ru.kabanchik.client.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import ru.kabanchik.client.feature.profile.api.ClientProfileComponent
import ru.kabanchik.client.feature.profile.api.ClientProfileDependencies
import ru.kabanchik.common.snackBar.api.SnackBarData
import ru.kabanchik.feature.client.chatDetails.api.flow.ClientChatFlowComponent
import ru.kabanchik.feature.client.chatDetails.api.flow.ClientChatFlowDependencies

internal class DefaultHomeComponent(
    componentContext: ComponentContext,
    private val showSnackBar: (SnackBarData) -> Unit,
    private val navigateAuth: () -> Unit
) : HomeComponent, ComponentContext by componentContext, KoinComponent {
    private val stackNavigation = StackNavigation<Config>()
    override val stack: Value<ChildStack<*, HomeComponent.Child>> = childStack(
        source = stackNavigation,
        serializer = Config.serializer(),
        initialStack = { listOf(Config.Chats) },
        childFactory = ::createChild
    )

    override fun onTabSelected(tab: HomeComponent.Tab) {
        val config = when (tab) {
            HomeComponent.Tab.Chats -> Config.Chats
            HomeComponent.Tab.Profile -> Config.Profile
        }
        stackNavigation.bringToFront(config)
    }

    private fun createChild(config: Config, context: ComponentContext): HomeComponent.Child {
        return when (config) {
            Config.Chats -> {
                HomeComponent.Child.Chats(
                    component = ClientChatFlowComponent.create(
                        componentContext = context,
                        showSnackBar = showSnackBar,
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
            Config.Profile -> {
                HomeComponent.Child.Profile(
                    component = ClientProfileComponent.create(
                        componentContext = context,
                        dependencies = ClientProfileDependencies.Factory(
                            profileInteractor = get(),
                            errorHandler = get()
                        ),
                        showSnackBar = showSnackBar,
                        navigateAuth = navigateAuth
                    )
                )
            }
        }
    }

    @Serializable
    private sealed class Config {
        @Serializable
        data object Chats : Config()
        @Serializable
        data object Profile : Config()
    }
}
