package ru.kabanchik.client.feature.auth.internal.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import ru.kabanchik.client.feature.auth.api.auth.AuthComponent

@Composable
internal fun AuthScreen(component: AuthComponent) {
    Box(
        modifier = Modifier.fillMaxSize().background(Color.Red)
    )
}