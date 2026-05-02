package ru.kabanchik.pro

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import ru.kabanchik.common.scaffold.ScaffoldState
import ru.kabanchik.common.scaffold.toolbar.CommonToolbar
import ru.kabanchik.common.snackBar.api.CommonSnackBarHost
import ru.kabanchik.pro.component.ProRootComponent
import ru.kabanchik.pro.feature.auth.api.ProAuthScreen
import ru.kabanchik.pro.feature.chat.api.flow.ProChatFlowScreen
import ru.kabanchik.pro.feature.splash.api.ProSplashScreen

@Composable
fun RootScreen(component: ProRootComponent) {
    val stack by component.stack.subscribeAsState()

    val toolbar by ScaffoldState.toolbar.collectAsState()

    Scaffold(
        snackbarHost = { CommonSnackBarHost(component.snackBarComponent.hostState) },
        topBar = { CommonToolbar(toolbar) }
    ) { paddingValues ->
        Children(
            stack = stack,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val child = it.instance) {
                is ProRootComponent.Child.Splash -> ProSplashScreen(component = child.component)
                is ProRootComponent.Child.Auth -> ProAuthScreen(component = child.component)
                is ProRootComponent.Child.Chat -> ProChatFlowScreen(component = child.component)
            }
        }
    }
}