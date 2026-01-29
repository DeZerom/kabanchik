package ru.kabanchik.client.feature.auth.api.flow

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import ru.kabanchik.client.feature.auth.api.auth.AuthComponent
import ru.kabanchik.client.feature.auth.api.register.RegisterComponent
import ru.kabanchik.client.feature.auth.internal.flow.DefaultFlowComponent
import ru.kabanchik.common.snackBar.api.SnackBarData

interface AuthFlowComponent {
    val stack: Value<ChildStack<*, Child>>

    sealed interface Child {
        class Auth(val component: AuthComponent) : Child
        class Register(val component: RegisterComponent) : Child
    }

    companion object {
        fun create(
            componentContext: ComponentContext,
            dependencies: AuthFlowDependencies,
            showSnackBar: (SnackBarData) -> Unit
        ): AuthFlowComponent {
            return DefaultFlowComponent(
                componentContext = componentContext,
                dependencies = dependencies,
                showSnackBar = showSnackBar
            )
        }
    }
}