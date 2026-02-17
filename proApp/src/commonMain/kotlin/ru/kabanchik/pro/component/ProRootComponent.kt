package ru.kabanchik.pro.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import ru.kabanchik.common.snackBar.api.SnackBarComponent
import ru.kabanchik.pro.feature.auth.api.ProAuthComponent
import ru.kabanchik.pro.feature.chatDetails.api.ProChatDetailsComponent

interface ProRootComponent {
    val stack: Value<ChildStack<*, Child>>
    val snackBarComponent: SnackBarComponent

    sealed interface Child {
        class Auth(val component: ProAuthComponent) : Child
        class ChatDetails(val component: ProChatDetailsComponent) : Child
    }

    companion object Companion {
        fun create(componentContext: ComponentContext): ProRootComponent {
            return DefaultProRootComponent(componentContext)
        }
    }
}