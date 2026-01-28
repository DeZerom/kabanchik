package ru.kabanchik.client.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import ru.kabanchik.client.feature.auth.api.flow.AuthFlowComponent
import ru.kabanchik.feature.client.chatDetails.api.ChatDetailsComponent

interface RootComponent {
    val stack: Value<ChildStack<*, Child>>

    sealed interface Child {
        data class Auth(val component: AuthFlowComponent) : Child
        data class Chat(val component: ChatDetailsComponent) : Child
    }

    companion object {
        fun create(componentContext: ComponentContext): RootComponent {
            return DefaultRootComponent(
                componentContext = componentContext
            )
        }
    }
}