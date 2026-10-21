package ru.kabanchik.client.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import ru.kabanchik.client.feature.auth.api.flow.AuthFlowComponent
import ru.kabanchik.client.feature.splash.api.ClientSplashComponent
import ru.kabanchik.common.snackBar.api.SnackBarComponent

interface RootComponent {
    val stack: Value<ChildStack<*, Child>>
    val snackBarComponent: SnackBarComponent

    sealed interface Child {
        class Splash(val component: ClientSplashComponent) : Child
        class Auth(val component: AuthFlowComponent) : Child
        class Home(val component: HomeComponent) : Child
    }

    companion object {
        fun create(componentContext: ComponentContext): RootComponent {
            return DefaultRootComponent(
                componentContext = componentContext
            )
        }
    }
}