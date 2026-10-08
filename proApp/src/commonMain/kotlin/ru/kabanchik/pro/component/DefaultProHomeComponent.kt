package ru.kabanchik.pro.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import ru.kabanchik.common.snackBar.api.SnackBarData
import ru.kabanchik.pro.feature.chat.api.flow.ProChatFlowComponent
import ru.kabanchik.pro.feature.chat.api.flow.ProChatFlowDependencies
import ru.kabanchik.pro.feature.profile.api.ProProfileComponent
import ru.kabanchik.pro.feature.profile.api.ProProfileDependencies

internal class DefaultProHomeComponent(
    componentContext: ComponentContext,
    private val showSnackBar: (SnackBarData) -> Unit,
    private val navigateAuth: () -> Unit
) : ProHomeComponent, ComponentContext by componentContext, KoinComponent {
    private val stackNavigation = StackNavigation<Config>()
    override val stack: Value<ChildStack<*, ProHomeComponent.Child>> = childStack(
        source = stackNavigation,
        serializer = Config.serializer(),
        initialStack = { listOf(Config.Chats) },
        childFactory = ::createChild
    )

    override fun onTabSelected(tab: ProHomeComponent.Tab) {
        val config = when (tab) {
            ProHomeComponent.Tab.Chats -> Config.Chats
            ProHomeComponent.Tab.Profile -> Config.Profile
        }
        stackNavigation.bringToFront(config)
    }

    private fun createChild(config: Config, context: ComponentContext): ProHomeComponent.Child {
        return when (config) {
            Config.Chats -> {
                ProHomeComponent.Child.Chats(
                    component = ProChatFlowComponent.create(
                        componentContext = context,
                        showSnackBar = showSnackBar,
                        dependencies = ProChatFlowDependencies.Factory(
                            chatsListInteractor = get(),
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
                ProHomeComponent.Child.Profile(
                    component = ProProfileComponent.create(
                        componentContext = context,
                        dependencies = ProProfileDependencies.Factory(
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
