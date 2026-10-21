package ru.kabanchik.pro.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.replaceAll
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.arkivanov.essenty.lifecycle.doOnStart
import dev.shivathapaa.logger.api.loggerE
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import ru.kabanchik.common.domain.profile.logic.api.CommonProfileInteractor
import ru.kabanchik.common.errorHandler.logic.api.ErrorType
import ru.kabanchik.common.network.api.SessionExpirationNotifier
import ru.kabanchik.common.network.api.StompConnectionController
import ru.kabanchik.common.snackBar.api.SnackBarComponent
import ru.kabanchik.common.snackBar.api.SnackBarData.Error
import ru.kabanchik.pro.domain.shift.logic.api.ProShiftInteractor
import ru.kabanchik.pro.feature.auth.api.ProAuthComponent
import ru.kabanchik.pro.feature.auth.api.ProAuthDependencies.Factory
import ru.kabanchik.pro.feature.shift.api.ProShiftGateComponent
import ru.kabanchik.pro.feature.shift.api.ProShiftGateDependencies
import ru.kabanchik.pro.feature.splash.api.ProSplashComponent
import ru.kabanchik.pro.feature.splash.api.ProSplashDependencies

class DefaultProRootComponent(
    componentContext: ComponentContext
) : ProRootComponent, ComponentContext by componentContext, KoinComponent {
    private val coroutineScope = coroutineScope()
    private val stackNavigation = StackNavigation<Config>()
    override val stack: Value<ChildStack<*, ProRootComponent.Child>> = childStack(
        source = stackNavigation,
        serializer = Config.serializer(),
        initialStack = { listOf(Config.Splash) },
        childFactory = ::createChild
    )

    override val snackBarComponent: SnackBarComponent = SnackBarComponent.create(
        componentContext = childContext("pro_snack_bar")
    )

    init {
        // На Android сокет рвется, пока приложение свернуто: при возврате переподключаемся сразу
        val connectionController = get<StompConnectionController>()
        lifecycle.doOnStart { connectionController.onAppForeground() }
        observeSessionExpiration()
    }

    // Сервер отклонил токен: чистим данные пользователя и отправляем на авторизацию
    private fun observeSessionExpiration() {
        val profileInteractor = get<CommonProfileInteractor>()
        val shiftInteractor = get<ProShiftInteractor>()
        get<SessionExpirationNotifier>().sessionExpired.onEach {
            when (stack.value.active.configuration) {
                Config.Splash, Config.Auth -> return@onEach
                else -> Unit
            }

            try {
                profileInteractor.logout()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Throwable) {
                loggerE("Logout after session expiration failed", e)
            }
            shiftInteractor.clear()
            stackNavigation.replaceAll(Config.Auth)
            snackBarComponent.setData(Error(ErrorType.SessionExpired.defaultMessage))
        }.launchIn(coroutineScope)
    }

    private fun createChild(config: Config, componentContext: ComponentContext): ProRootComponent.Child {
        return when (config) {
            Config.Splash -> {
                ProRootComponent.Child.Splash(
                    component = ProSplashComponent.create(
                        componentContext = componentContext,
                        dependencies = ProSplashDependencies.Factory(
                            splashInteractor = get()
                        ),
                        navigateAuth = { stackNavigation.replaceAll(Config.Auth) },
                        navigateChatsList = { stackNavigation.replaceAll(Config.ShiftGate) }
                    )
                )
            }
            Config.Auth -> {
                ProRootComponent.Child.Auth(
                    component = ProAuthComponent.create(
                        componentContext = componentContext,
                        dependencies = Factory(
                            authInteractor = get(),
                            errorHandler = get()
                        ),
                        onAuthorize = { stackNavigation.replaceAll(Config.ShiftGate) },
                        onError = { snackBarComponent.setData(Error(it)) },
                    )
                )
            }
            Config.ShiftGate -> {
                ProRootComponent.Child.ShiftGate(
                    component = ProShiftGateComponent.create(
                        componentContext = componentContext,
                        dependencies = ProShiftGateDependencies.Factory(
                            shiftInteractor = get(),
                            errorHandler = get()
                        ),
                        showSnackBar = { snackBarComponent.setData(it) },
                        navigateHome = { stackNavigation.replaceAll(Config.Home) },
                        navigateAuth = { stackNavigation.replaceAll(Config.Auth) }
                    )
                )
            }
            Config.Home -> {
                ProRootComponent.Child.Home(
                    component = ProHomeComponent.create(
                        componentContext = componentContext,
                        showSnackBar = { snackBarComponent.setData(it) },
                        navigateAuth = { stackNavigation.replaceAll(Config.Auth) },
                        navigateShiftGate = { stackNavigation.replaceAll(Config.ShiftGate) }
                    )
                )
            }
        }
    }

    @Serializable
    sealed class Config {
        @Serializable
        data object Splash : Config()

        @Serializable
        data object Auth : Config()

        @Serializable
        data object ShiftGate : Config()

        @Serializable
        data object Home : Config()
    }
}
