package ru.kabanchik.client

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import ru.kabanchik.client.component.RootComponent

@Composable
@Preview
fun App(rootComponent: RootComponent) {
    MaterialTheme {
        RootScreen(rootComponent)
    }
}