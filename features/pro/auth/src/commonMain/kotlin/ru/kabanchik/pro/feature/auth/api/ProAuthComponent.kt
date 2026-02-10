package ru.kabanchik.pro.feature.auth.api

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.StateFlow
import ru.kabanchik.common.tools.textResource.TextResource
import ru.kabanchik.pro.feature.auth.internal.DefaultProAuthComponent

interface ProAuthComponent {
    val uiState: StateFlow<ProAuthContract.State>

    fun onAuthorizeClicked()
    fun onLoginChanged(newLogin: String)
    fun onPasswordChanged(newPassword: String)

    companion object Companion {
        fun create(
            componentContext: ComponentContext,
            dependencies: ProAuthDependencies,
            onAuthorize: () -> Unit,
            onError: (TextResource) -> Unit
        ): ProAuthComponent {
            return DefaultProAuthComponent(
                componentContext = componentContext,
                dependencies = dependencies,
                onAuthorize = onAuthorize,
                onError = onError
            )
        }
    }
}