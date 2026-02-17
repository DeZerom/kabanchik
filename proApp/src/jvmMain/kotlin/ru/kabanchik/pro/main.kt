package ru.kabanchik.pro

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import org.koin.core.context.startKoin
import ru.kabanchik.common.screenSize.LocalWindowSizeCompositionProvider
import ru.kabanchik.pro.component.ProRootComponent
import ru.kabanchik.pro.di.proAppModules
import javax.swing.SwingUtilities

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
fun main() {
    startKoin {
        modules(proAppModules())
    }

    val lifecycle = LifecycleRegistry()
    val rootComponent = runOnUiThread {
        ProRootComponent.create(
            componentContext = DefaultComponentContext(lifecycle = lifecycle)
        )
    }

    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "kabanchik.pro",
        ) {
            val windowSize = calculateWindowSizeClass()
            CompositionLocalProvider(
                LocalWindowSizeCompositionProvider provides windowSize
            ) {
                App(rootComponent)
            }
        }
    }
}

private fun <T> runOnUiThread(block: () -> T): T {
    if (SwingUtilities.isEventDispatchThread()) {
        return block()
    }

    var error: Throwable? = null
    var result: T? = null

    SwingUtilities.invokeAndWait {
        try {
            result = block()
        } catch (e: Throwable) {
            error = e
        }
    }

    error?.also { throw it }

    @Suppress("UNCHECKED_CAST")
    return result as T
}