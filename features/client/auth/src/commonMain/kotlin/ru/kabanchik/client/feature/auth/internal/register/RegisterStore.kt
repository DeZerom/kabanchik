package ru.kabanchik.client.feature.auth.internal.register

import kabanchik.features.client.auth.generated.resources.Res
import kabanchik.features.client.auth.generated.resources.auth_reg_empty_creds_error
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import ru.kabanchik.client.domain.auth.logic.api.AuthInteractor
import ru.kabanchik.client.feature.auth.api.register.RegisterContract.Event
import ru.kabanchik.client.feature.auth.api.register.RegisterContract.SideEffect
import ru.kabanchik.client.feature.auth.api.register.RegisterContract.State
import ru.kabanchik.common.errorHandler.logic.api.ErrorHandler
import ru.kabanchik.common.store.BaseCoroutineStore
import ru.kabanchik.common.tools.textResource.TextResource

internal class RegisterStore(
    private val authInteractor: AuthInteractor,
    private val errorHandler: ErrorHandler
) : BaseCoroutineStore<Event, State, SideEffect>() {
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, error ->
        pushSideEffect(SideEffect.Error(createErrorText(errorHandler.handleError(error))))
    }

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
        if (currentState.login.isBlank() || currentState.password.isBlank()) {
            pushSideEffect(SideEffect.Error(TextResource.Id(Res.string.auth_reg_empty_creds_error)))
            return
        }

        coroutineScope.launch(coroutineExceptionHandler) {
            reduceState { copy(isLoading = true) }
            authInteractor.register(
                login = currentState.login,
                password = currentState.password
            )
            reduceState { initState() }
            pushSideEffect(SideEffect.Success)
        }.invokeOnCompletion {
            reduceState { copy(isLoading = false) }
        }
    }
}