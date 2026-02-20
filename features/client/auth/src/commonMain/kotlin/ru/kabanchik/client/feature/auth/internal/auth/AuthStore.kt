package ru.kabanchik.client.feature.auth.internal.auth

import kabanchik.features.client.auth.generated.resources.Res
import kabanchik.features.client.auth.generated.resources.auth_auth_empty_creds_error
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import ru.kabanchik.client.domain.auth.logic.api.AuthInteractor
import ru.kabanchik.client.feature.auth.api.auth.AuthContract.Event
import ru.kabanchik.client.feature.auth.api.auth.AuthContract.SideEffect
import ru.kabanchik.client.feature.auth.api.auth.AuthContract.State
import ru.kabanchik.common.errorHandler.logic.api.ErrorHandler
import ru.kabanchik.common.store.BaseCoroutineStore
import ru.kabanchik.common.tools.textResource.TextResource

class AuthStore(
    private val authInteractor: AuthInteractor,
    private val errorHandler: ErrorHandler
) : BaseCoroutineStore<Event, State, SideEffect>() {
    private val coroutineErrorHandler = CoroutineExceptionHandler { _, error ->
        pushSideEffect(SideEffect.Error(errorHandler.handleError(error).defaultMessage))
    }

    override fun initState(): State {
        return State()
    }

    override fun handleEvent(event: Event) {
        when (event) {
            is Event.LoginChanged -> reduceState { copy(login = event.newLogin) }
            is Event.PasswordChanged -> reduceState { copy(password = event.newPassword) }
            Event.AuthorizeClicked -> authorize()
            Event.RegisterClicked -> onRegisterClicked()
        }
    }

    private fun onRegisterClicked() {
        reduceState { initState() }
        pushSideEffect(SideEffect.NavigateRegistration)
    }

    private fun authorize() {
        if (currentState.login.isEmpty() || currentState.password.isEmpty()) {
            pushSideEffect(SideEffect.Error(TextResource.Id(Res.string.auth_auth_empty_creds_error)))
            return
        }

        coroutineScope.launch(coroutineErrorHandler) {
            reduceState { copy(isLoading = true) }
            authInteractor.authorize(
                login = currentState.login,
                password = currentState.password
            )
            pushSideEffect(SideEffect.Success)
            reduceState { initState() }
        }.invokeOnCompletion {
            reduceState { copy(isLoading = false) }
        }
    }
}