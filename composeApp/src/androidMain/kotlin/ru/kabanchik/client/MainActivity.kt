package ru.kabanchik.client

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.arkivanov.decompose.defaultComponentContext
import ru.kabanchik.client.component.RootComponent

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        val rootComponent = RootComponent.create(
            componentContext = defaultComponentContext()
        )

        setContent {
            App(rootComponent)
        }
    }
}