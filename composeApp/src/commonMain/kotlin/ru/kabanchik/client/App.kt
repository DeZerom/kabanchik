package ru.kabanchik.client

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import ru.kabanchik.client.component.RootComponent
import ru.kabanchik.common.uiKit.theme.KabanchikTheme

@Composable
@Preview
fun App(rootComponent: RootComponent) {
    KabanchikTheme {
        RootScreen(rootComponent)
    }
}