package ru.kabanchik.client.feature.auth.api.flow

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import ru.kabanchik.client.feature.auth.internal.auth.ClientAuthScreen
import ru.kabanchik.client.feature.auth.internal.register.ClientRegisterScreen

@Composable
fun AuthFlowScreen(component: AuthFlowComponent) {
    val stack by component.stack.subscribeAsState()

    Children(
        stack = stack
    ) {
        when (val child = it.instance) {
            is AuthFlowComponent.Child.Auth -> {
                ClientAuthScreen(child.component)
            }
            is AuthFlowComponent.Child.Register -> {
                ClientRegisterScreen(child.component)
            }
        }
    }
}