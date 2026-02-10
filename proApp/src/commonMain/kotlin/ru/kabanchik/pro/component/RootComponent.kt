package ru.kabanchik.pro.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import ru.kabanchik.common.snackBar.api.SnackBarComponent
import ru.kabanchik.pro.feature.auth.api.ProAuthComponent

interface RootComponent {
    val stack: Value<ChildStack<*, Child>>
    val snackBarComponent: SnackBarComponent

    sealed interface Child {
        class Auth(val component: ProAuthComponent) : Child
    }

    companion object {
        fun create(componentContext: ComponentContext): RootComponent {
            return DefaultRootComponent(componentContext)
        }
    }
}