package ru.kabanchik.client

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import ru.kabanchik.client.chatDetails.api.ChatDetailsScreen
import ru.kabanchik.client.component.RootComponent

@Composable
fun RootScreen(component: RootComponent) {
    val stack by component.stack.subscribeAsState()

    Children(
        stack = stack,
        modifier = Modifier.fillMaxSize()
    ) {
        when (val child = it.instance) {
            is RootComponent.Child.Chat -> ChatDetailsScreen(component = child.component)
        }
    }
}