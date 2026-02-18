package ru.kabanchik.pro.feature.auth.internal

import kabanchik.features.pro.auth.generated.resources.Res
import kabanchik.features.pro.auth.generated.resources.pro_auth_auth_empty_creds_error
import kotlinx.coroutines.launch
import ru.kabanchik.common.store.BaseCoroutineStore
import ru.kabanchik.common.tools.textResource.TextResource
import ru.kabanchik.pro.domain.auth.logic.api.ProAuthInteractor
import ru.kabanchik.pro.feature.auth.api.ProAuthContract.Event
import ru.kabanchik.pro.feature.auth.api.ProAuthContract.SideEffect
import ru.kabanchik.pro.feature.auth.api.ProAuthContract.State

class ProAuthStore(
    private val authInteractor: ProAuthInteractor,
) : BaseCoroutineStore<Event, State, SideEffect>() {
    override fun initState(): State {
        return State()
    }

    override fun handleEvent(event: Event) {
        when (event) {
            Event.AuthorizeClicked -> authorizeClicked()
            is Event.LoginChanged -> reduceState { copy(login = event.newLogin) }
            is Event.PasswordChanged -> reduceState { copy(password = event.newPassword) }
        }
    }

    private fun authorizeClicked() {
        if (currentState.login.isEmpty() || currentState.password.isEmpty()) {
            pushSideEffect(SideEffect.Error(TextResource.Id(Res.string.pro_auth_auth_empty_creds_error)))
            return
        }

        coroutineScope.launch {
            reduceState { copy(isLoading = true) }
            authInteractor.authorize(
                login = currentState.login,
                password = currentState.password
            )
            pushSideEffect(SideEffect.Authorized)

            reduceState { copy(isLoading = false) }
        }
    }
}