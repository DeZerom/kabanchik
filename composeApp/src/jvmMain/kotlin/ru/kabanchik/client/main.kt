package ru.kabanchik.client

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import ru.kabanchik.client.component.RootComponent
import javax.swing.SwingUtilities

fun main() {
    val lifecycle = LifecycleRegistry()
    val rootComponent = runOnUiThread {
        RootComponent.create(
            componentContext = DefaultComponentContext(lifecycle = lifecycle)
        )
    }

    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "kabanchik",
        ) {
            App(rootComponent)
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