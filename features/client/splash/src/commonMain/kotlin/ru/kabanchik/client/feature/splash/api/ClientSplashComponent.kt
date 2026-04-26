package ru.kabanchik.client.feature.splash.api

import com.arkivanov.decompose.ComponentContext
import ru.kabanchik.client.feature.splash.internal.DefaultClientSplashComponent

interface ClientSplashComponent {
    companion object {
        fun create(
            componentContext: ComponentContext,
            dependencies: ClientSplashDependencies,
            navigateChatsList: () -> Unit,
            navigateAuth: () -> Unit
        ): ClientSplashComponent {
            return DefaultClientSplashComponent(
                componentContext = componentContext,
                dependencies = dependencies,
                navigateChatsList = navigateChatsList,
                navigateAuth = navigateAuth
            )
        }
    }
}