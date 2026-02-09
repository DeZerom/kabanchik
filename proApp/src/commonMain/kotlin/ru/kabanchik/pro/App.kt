package ru.kabanchik.pro

import androidx.compose.runtime.Composable
import ru.kabanchik.common.uiKit.theme.KabanchikTheme
import ru.kabanchik.pro.component.RootComponent

@Composable
fun App(rootComponent: RootComponent) {
    KabanchikTheme {
        RootScreen(rootComponent)
    }
}