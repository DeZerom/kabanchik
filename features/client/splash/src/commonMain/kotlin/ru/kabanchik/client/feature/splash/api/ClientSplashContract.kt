package ru.kabanchik.client.feature.splash.api

interface ClientSplashContract {
    class State() : ClientSplashContract

    sealed interface Event : ClientSplashContract

    sealed interface SideEffect {
        object NotAuthorized : SideEffect
        object Authorized : SideEffect
    }
}