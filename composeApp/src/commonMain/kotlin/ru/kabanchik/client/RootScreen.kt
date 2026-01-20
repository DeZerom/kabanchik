package ru.kabanchik.client

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import ru.kabanchik.client.chatDetails.api.ChatDetailsScreen
import ru.kabanchik.client.component.RootComponent

@Composable
fun RootScreen(component: RootComponent) {
    val stack by component.stack.subscribeAsState()

    Children(stack) {
        when (val child = it.instance) {
            is RootComponent.Child.Chat -> ChatDetailsScreen()
        }
    }
}