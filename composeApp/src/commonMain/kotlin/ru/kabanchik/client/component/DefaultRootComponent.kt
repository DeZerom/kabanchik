package ru.kabanchik.client.component

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
import ru.kabanchik.client.feature.auth.api.flow.AuthFlowComponent
import ru.kabanchik.client.feature.auth.api.flow.AuthFlowDependencies
import ru.kabanchik.client.feature.splash.api.ClientSplashComponent
import ru.kabanchik.client.feature.splash.api.ClientSplashDependencies
import ru.kabanchik.common.domain.profile.logic.api.CommonProfileInteractor
import ru.kabanchik.common.errorHandler.logic.api.ErrorType
import ru.kabanchik.common.network.api.SessionExpirationNotifier
import ru.kabanchik.common.network.api.StompConnectionController
import ru.kabanchik.common.snackBar.api.SnackBarComponent
import ru.kabanchik.common.snackBar.api.SnackBarData

class DefaultRootComponent(
    componentContext: ComponentContext
) : RootComponent, ComponentContext by componentContext, KoinComponent {
    private val coroutineScope = coroutineScope()
    private val stackNavigation = StackNavigation<Config>()
    override val stack: Value<ChildStack<*, RootComponent.Child>> = childStack(
        source = stackNavigation,
        serializer = Config.serializer(),
        initialStack = { listOf(Config.Splash) },
        childFactory = ::createChild,
    )

    override val snackBarComponent: SnackBarComponent = SnackBarComponent.create(
        componentContext = childContext("root_snack_bar_component")
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
            stackNavigation.replaceAll(Config.Auth)
            snackBarComponent.setData(SnackBarData.Error(ErrorType.SessionExpired.defaultMessage))
        }.launchIn(coroutineScope)
    }

    private fun createChild(config: Config, context: ComponentContext): RootComponent.Child {
        return when (config) {
            Config.Splash -> {
                RootComponent.Child.Splash(
                    component = ClientSplashComponent.create(
                        componentContext = context,
                        dependencies = ClientSplashDependencies.Factory(
                            splashInteractor = get()
                        ),
                        navigateChatsList = { stackNavigation.replaceAll(Config.Home) },
                        navigateAuth = { stackNavigation.replaceAll(Config.Auth) }
                    )
                )
            }
            Config.Auth -> {
                RootComponent.Child.Auth(
                    component = AuthFlowComponent.create(
                        componentContext = context,
                        dependencies = AuthFlowDependencies.Factory(
                            authInteractor = get(),
                            errorHandler = get()
                        ),
                        showSnackBar = { snackBarComponent.setData(it) },
                        navigateHome = { stackNavigation.replaceAll(Config.Home) }
                    )
                )
            }
            Config.Home -> {
                RootComponent.Child.Home(
                    component = HomeComponent.create(
                        componentContext = context,
                        showSnackBar = { snackBarComponent.setData(it) },
                        navigateAuth = { stackNavigation.replaceAll(Config.Auth) }
                    )
                )
            }
        }
    }

    @Serializable
    private sealed class Config {
        @Serializable
        data object Splash : Config()
        @Serializable
        data object Auth : Config()
        @Serializable
        data object Home : Config()
    }
}
