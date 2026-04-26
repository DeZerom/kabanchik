package ru.kabanchik.pro.feature.splash.api

import com.arkivanov.decompose.ComponentContext
import ru.kabanchik.pro.feature.splash.internal.DefaultProSplashComponent

interface ProSplashComponent {
    companion object {
        fun create(
            componentContext: ComponentContext,
            dependencies: ProSplashDependencies,
            navigateChatsList: () -> Unit,
            navigateAuth: () -> Unit
        ): ProSplashComponent {
            return DefaultProSplashComponent(
                componentContext = componentContext,
                dependencies = dependencies,
                navigateChatsList = navigateChatsList,
                navigateAuth = navigateAuth
            )
        }
    }
}