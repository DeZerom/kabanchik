package ru.kabanchik.pro.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import ru.kabanchik.common.snackBar.api.SnackBarComponent
import ru.kabanchik.pro.feature.auth.api.ProAuthComponent
import ru.kabanchik.pro.feature.shift.api.ProShiftGateComponent
import ru.kabanchik.pro.feature.splash.api.ProSplashComponent

interface ProRootComponent {
    val stack: Value<ChildStack<*, Child>>
    val snackBarComponent: SnackBarComponent

    sealed interface Child {
        class Splash(val component: ProSplashComponent) : Child
        class Auth(val component: ProAuthComponent) : Child
        class ShiftGate(val component: ProShiftGateComponent) : Child
        class Home(val component: ProHomeComponent) : Child
    }

    companion object Companion {
        fun create(componentContext: ComponentContext): ProRootComponent {
            return DefaultProRootComponent(componentContext)
        }
    }
}