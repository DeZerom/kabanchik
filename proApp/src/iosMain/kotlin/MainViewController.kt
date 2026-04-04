package ru.kabanchik.client

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.ApplicationLifecycle
import ru.kabanchik.common.screenSize.LocalWindowSizeCompositionProvider
import ru.kabanchik.pro.App
import ru.kabanchik.pro.component.ProRootComponent

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
fun MainViewController() = ComposeUIViewController {
    val rootComponent = remember {
        ProRootComponent.create(
            componentContext = DefaultComponentContext(ApplicationLifecycle())
        )
    }

    val windowSize = calculateWindowSizeClass()

    CompositionLocalProvider(
        LocalWindowSizeCompositionProvider provides windowSize
    ) {
        App(rootComponent)
    }
}