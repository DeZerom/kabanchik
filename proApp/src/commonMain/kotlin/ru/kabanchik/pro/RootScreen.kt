package ru.kabanchik.pro

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import ru.kabanchik.common.snackBar.api.CommonSnackBarHost
import ru.kabanchik.pro.component.ProRootComponent
import ru.kabanchik.pro.feature.auth.api.ProAuthScreen
import ru.kabanchik.pro.feature.chatDetails.api.ProChatDetailsScreen

@Composable
fun RootScreen(component: ProRootComponent) {
    val stack by component.stack.subscribeAsState()

    Scaffold(
        snackbarHost = { CommonSnackBarHost(component.snackBarComponent.hostState) }
    ) {
        Children(
            stack = stack,
            modifier = Modifier.fillMaxSize()
        ) {
            when (val child = it.instance) {
                is ProRootComponent.Child.Auth -> ProAuthScreen(component = child.component)
                is ProRootComponent.Child.ChatDetails -> ProChatDetailsScreen(component = child.component)
            }
        }
    }
}