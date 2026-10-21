package ru.kabanchik.pro.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.arkivanov.essenty.lifecycle.doOnStart
import dev.shivathapaa.logger.api.loggerE
import kabanchik.proapp.generated.resources.Res
import kabanchik.proapp.generated.resources.home_shift_ended
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import ru.kabanchik.common.snackBar.api.SnackBarData
import ru.kabanchik.common.tools.extensions.asTextResource
import ru.kabanchik.pro.domain.chat.logic.api.ProChatsListInteractor
import ru.kabanchik.pro.domain.shift.logic.api.ProShiftInteractor
import ru.kabanchik.pro.domain.shift.model.ProShiftStatus
import ru.kabanchik.pro.feature.chat.api.flow.ProChatFlowComponent
import ru.kabanchik.pro.feature.chat.api.flow.ProChatFlowDependencies
import ru.kabanchik.pro.feature.profile.api.ProProfileComponent
import ru.kabanchik.pro.feature.profile.api.ProProfileDependencies

internal class DefaultProHomeComponent(
    componentContext: ComponentContext,
    private val showSnackBar: (SnackBarData) -> Unit,
    private val navigateAuth: () -> Unit,
    private val navigateShiftGate: () -> Unit
) : ProHomeComponent, ComponentContext by componentContext, KoinComponent {
    private val coroutineScope = coroutineScope()
    private val shiftInteractor = get<ProShiftInteractor>()
    private val stackNavigation = StackNavigation<Config>()
    override val stack: Value<ChildStack<*, ProHomeComponent.Child>> = childStack(
        source = stackNavigation,
        serializer = Config.serializer(),
        initialStack = { listOf(Config.Chats) },
        childFactory = ::createChild
    )

    init {
        observeShift()
    }

    override fun onTabSelected(tab: ProHomeComponent.Tab) {
        val config = when (tab) {
            ProHomeComponent.Tab.Chats -> Config.Chats
            ProHomeComponent.Tab.Profile -> Config.Profile
        }
        stackNavigation.bringToFront(config)
    }

    // Смена могла завершиться здесь или на другом устройстве: тогда работа с чатами недоступна
    private fun observeShift() {
        shiftInteractor.shiftStatus
            .filter { it == ProShiftStatus.OffShift }
            .onEach {
                showSnackBar(SnackBarData.Success(Res.string.home_shift_ended.asTextResource()))
                navigateShiftGate()
            }
            .launchIn(coroutineScope)

        // Отдельного события о смене нет: перепроверяем при возврате в приложение и переподключении.
        // При первом старте статус уже известен, если не восстановились после смерти процесса
        var isFirstStart = true
        lifecycle.doOnStart {
            if (!isFirstStart || shiftInteractor.shiftStatus.value == null) {
                refreshShift()
            }
            isFirstStart = false
        }
        coroutineScope.launch {
            get<ProChatsListInteractor>().listenReconnections().collect { refreshShift() }
        }
    }

    private fun refreshShift() {
        coroutineScope.launch {
            try {
                shiftInteractor.refresh()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Throwable) {
                // Истекшую сессию обработает root, остальные ошибки проверим при следующем случае
                loggerE("Shift refresh failed", e)
            }
        }
    }

    private fun createChild(config: Config, context: ComponentContext): ProHomeComponent.Child {
        return when (config) {
            Config.Chats -> {
                ProHomeComponent.Child.Chats(
                    component = ProChatFlowComponent.create(
                        componentContext = context,
                        showSnackBar = showSnackBar,
                        dependencies = ProChatFlowDependencies.Factory(
                            chatsListInteractor = get(),
                            chatDetailsInteractor = get(),
                            userInteractor = get(),
                            errorHandler = get(),
                            filePicker = get(),
                            fileOpener = get(),
                        )
                    )
                )
            }
            Config.Profile -> {
                ProHomeComponent.Child.Profile(
                    component = ProProfileComponent.create(
                        componentContext = context,
                        dependencies = ProProfileDependencies.Factory(
                            profileInteractor = get(),
                            shiftInteractor = get(),
                            errorHandler = get()
                        ),
                        showSnackBar = showSnackBar,
                        navigateAuth = navigateAuth
                    )
                )
            }
        }
    }

    @Serializable
    private sealed class Config {
        @Serializable
        data object Chats : Config()
        @Serializable
        data object Profile : Config()
    }
}
