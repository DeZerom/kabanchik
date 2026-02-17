package ru.kabanchik.pro

import androidx.compose.runtime.Composable
import ru.kabanchik.common.uiKit.theme.KabanchikTheme
import ru.kabanchik.pro.component.ProRootComponent

@Composable
fun App(rootComponent: ProRootComponent) {
    KabanchikTheme {
        RootScreen(rootComponent)
    }
}