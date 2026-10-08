package ru.kabanchik.pro.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import ru.kabanchik.common.snackBar.api.SnackBarData
import ru.kabanchik.pro.feature.chat.api.flow.ProChatFlowComponent
import ru.kabanchik.pro.feature.profile.api.ProProfileComponent

interface ProHomeComponent {
    val stack: Value<ChildStack<*, Child>>

    fun onTabSelected(tab: Tab)

    enum class Tab {
        Chats,
        Profile
    }

    sealed interface Child {
        val tab: Tab

        class Chats(val component: ProChatFlowComponent) : Child {
            override val tab: Tab = Tab.Chats
        }

        class Profile(val component: ProProfileComponent) : Child {
            override val tab: Tab = Tab.Profile
        }
    }

    companion object {
        fun create(
            componentContext: ComponentContext,
            showSnackBar: (SnackBarData) -> Unit,
            navigateAuth: () -> Unit
        ): ProHomeComponent {
            return DefaultProHomeComponent(
                componentContext = componentContext,
                showSnackBar = showSnackBar,
                navigateAuth = navigateAuth
            )
        }
    }
}
