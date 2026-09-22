package ru.kabanchik.client

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.CompositionLocalProvider
import androidx.core.view.WindowCompat
import com.arkivanov.decompose.defaultComponentContext
import ru.kabanchik.client.component.RootComponent
import ru.kabanchik.common.screenSize.LocalWindowSizeCompositionProvider

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        val insetsController = WindowCompat.getInsetsController(window, window.decorView)
        insetsController.isAppearanceLightStatusBars = false
        insetsController.isAppearanceLightNavigationBars = false

        val rootComponent = RootComponent.create(
            componentContext = defaultComponentContext()
        )

        setContent {
            val windowSize = calculateWindowSizeClass(this)

            CompositionLocalProvider(
                LocalWindowSizeCompositionProvider provides windowSize
            ) {
                App(rootComponent)
            }
        }
    }
}