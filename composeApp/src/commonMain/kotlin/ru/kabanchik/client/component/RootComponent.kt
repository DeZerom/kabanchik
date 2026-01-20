package ru.kabanchik.client.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value

interface RootComponent {
    val stack: Value<ChildStack<*, Child>>

    sealed interface Child {
        data object Chat : Child
    }

    companion object {
        fun create(componentContext: ComponentContext): RootComponent {
            return DefaultRootComponent(
                componentContext = componentContext
            )
        }
    }
}