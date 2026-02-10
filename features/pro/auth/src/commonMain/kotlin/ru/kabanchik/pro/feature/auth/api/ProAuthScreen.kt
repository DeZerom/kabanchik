package ru.kabanchik.pro.feature.auth.api

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import ru.kabanchik.common.screenSize.PartFillingScreen
import ru.kabanchik.pro.feature.auth.internal.ProAuthContent

@Composable
fun ProAuthScreen(component: ProAuthComponent) {
    val state by component.uiState.collectAsState()

    PartFillingScreen {
        ProAuthContent(
            state = state,
            onAuthorizeClicked = component::onAuthorizeClicked,
            onLoginChanged = component::onLoginChanged,
            onPasswordChanged = component::onPasswordChanged
        )
    }
}