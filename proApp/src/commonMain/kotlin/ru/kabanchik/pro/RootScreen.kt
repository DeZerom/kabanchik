package ru.kabanchik.pro

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import ru.kabanchik.common.snackBar.api.CommonSnackBarHost
import ru.kabanchik.pro.component.RootComponent
import ru.kabanchik.pro.feature.auth.api.ProAuthScreen

@Composable
fun RootScreen(component: RootComponent) {
    val stack by component.stack.subscribeAsState()

    Scaffold(
        snackbarHost = { CommonSnackBarHost(component.snackBarComponent.hostState) }
    ) {
        Children(
            stack = stack,
            modifier = Modifier.fillMaxSize()
        ) {
            when (val child = it.instance) {
                is RootComponent.Child.Auth -> ProAuthScreen(component = child.component)
            }
        }
    }
}