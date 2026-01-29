package ru.kabanchik.client.feature.auth.internal.auth

import kabanchik.features.client.auth.generated.resources.Res
import kabanchik.features.client.auth.generated.resources.auth_auth_empty_creds_error
import kotlinx.coroutines.launch
import ru.kabanchik.client.domain.auth.logic.api.AuthInteractor
import ru.kabanchik.client.feature.auth.api.auth.AuthContract.Event
import ru.kabanchik.client.feature.auth.api.auth.AuthContract.SideEffect
import ru.kabanchik.client.feature.auth.api.auth.AuthContract.State
import ru.kabanchik.common.store.BaseCoroutineStore
import ru.kabanchik.common.tools.textResource.TextResource

class AuthStore(
    private val authInteractor: AuthInteractor
) : BaseCoroutineStore<Event, State, SideEffect>() {
    override fun initState(): State {
        return State()
    }

    override fun handleEvent(event: Event) {
        when (event) {
            is Event.LoginChanged -> reduceState { copy(login = event.newLogin) }
            is Event.PasswordChanged -> reduceState { copy(password = event.newPassword) }
            Event.AuthorizeClicked -> authorize()
        }
    }

    private fun authorize() {
        if (currentState.login.isEmpty() || currentState.password.isEmpty()) {
            pushSideEffect(SideEffect.Error(TextResource.Id(Res.string.auth_auth_empty_creds_error)))
            return
        }

        coroutineScope.launch {
            reduceState { copy(isLoading = true) }
            val error = authInteractor.authorize(
                login = currentState.login,
                password = currentState.password
            )
            if (error != null) {
                pushSideEffect(SideEffect.Error(TextResource.Raw(error)))
            } else {
                pushSideEffect(SideEffect.Success)
            }
            reduceState { copy(isLoading = false) }
        }
    }
}