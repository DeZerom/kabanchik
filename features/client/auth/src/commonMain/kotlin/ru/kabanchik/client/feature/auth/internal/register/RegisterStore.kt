package ru.kabanchik.client.feature.auth.internal.register

import ru.kabanchik.client.domain.auth.logic.api.AuthInteractor
import ru.kabanchik.client.feature.auth.api.register.RegisterContract.Event
import ru.kabanchik.client.feature.auth.api.register.RegisterContract.SideEffect
import ru.kabanchik.client.feature.auth.api.register.RegisterContract.State
import ru.kabanchik.common.store.BaseCoroutineStore

internal class RegisterStore(
    private val authInteractor: AuthInteractor
) : BaseCoroutineStore<Event, State, SideEffect>() {
    override fun initState(): State {
        return State()
    }

    override fun handleEvent(event: Event) {
        when (event) {
            is Event.LoginChanged -> reduceState { copy(login = event.newLogin) }
            is Event.PasswordChanged -> reduceState { copy(password = event.newPassword) }
            Event.CreateAccountClicked -> createAccount()
        }
    }

    private fun createAccount() {

    }
}