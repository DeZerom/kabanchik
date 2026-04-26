package ru.kabanchik.pro.feature.splash.api

interface ProSplashContract {
    class State() : ProSplashContract

    sealed interface Event : ProSplashContract

    sealed interface SideEffect {
        object NotAuthorized : SideEffect
        object Authorized : SideEffect
    }
}