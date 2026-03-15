package ru.kabanchik.client

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import ru.kabanchik.client.component.RootComponent
import ru.kabanchik.client.feature.auth.api.flow.AuthFlowScreen
import ru.kabanchik.common.snackBar.api.CommonSnackBarHost
import ru.kabanchik.feature.client.chatDetails.api.flow.ClientChatFlowScreen

@Composable
fun RootScreen(component: RootComponent) {
    val stack by component.stack.subscribeAsState()

    Scaffold(
        snackbarHost = { CommonSnackBarHost(component.snackBarComponent.hostState) }
    ) { paddingValues ->
        Children(
            stack = stack,
            animation = stackAnimation(),
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val child = it.instance) {
                is RootComponent.Child.Auth -> AuthFlowScreen(component = child.component)
                is RootComponent.Child.Chat -> ClientChatFlowScreen(component = child.component)
            }
        }
    }
}