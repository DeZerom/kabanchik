package ru.kabanchik.client.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import ru.kabanchik.client.feature.profile.api.ClientProfileComponent
import ru.kabanchik.common.snackBar.api.SnackBarData
import ru.kabanchik.feature.client.chatDetails.api.flow.ClientChatFlowComponent

interface HomeComponent {
    val stack: Value<ChildStack<*, Child>>

    fun onTabSelected(tab: Tab)

    enum class Tab {
        Chats,
        Profile
    }

    sealed interface Child {
        val tab: Tab

        class Chats(val component: ClientChatFlowComponent) : Child {
            override val tab: Tab = Tab.Chats
        }

        class Profile(val component: ClientProfileComponent) : Child {
            override val tab: Tab = Tab.Profile
        }
    }

    companion object {
        fun create(
            componentContext: ComponentContext,
            showSnackBar: (SnackBarData) -> Unit,
            navigateAuth: () -> Unit
        ): HomeComponent {
            return DefaultHomeComponent(
                componentContext = componentContext,
                showSnackBar = showSnackBar,
                navigateAuth = navigateAuth
            )
        }
    }
}
