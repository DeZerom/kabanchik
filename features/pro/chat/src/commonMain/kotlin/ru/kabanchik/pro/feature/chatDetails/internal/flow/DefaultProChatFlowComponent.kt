package ru.kabanchik.pro.feature.chatDetails.internal.flow

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import kotlinx.serialization.Serializable
import ru.kabanchik.common.snackBar.api.SnackBarData
import ru.kabanchik.pro.feature.chatDetails.api.details.ProChatDetailsDependencies
import ru.kabanchik.pro.feature.chatDetails.api.flow.ProChatFlowComponent
import ru.kabanchik.pro.feature.chatDetails.api.flow.ProChatFlowDependencies
import ru.kabanchik.pro.feature.chatDetails.internal.details.DefaultProChatDetailsComponent
import ru.kabanchik.pro.feature.chatDetails.internal.list.DefaultProChatsListComponent

class DefaultProChatFlowComponent(
    componentContext: ComponentContext,
    private val showSnackBar: (SnackBarData) -> Unit,
    private val dependencies: ProChatFlowDependencies
) : ProChatFlowComponent, ComponentContext by componentContext {
    private val stackNavigation = StackNavigation<Config>()
    override val stack: Value<ChildStack<*, ProChatFlowComponent.Child>> = childStack(
        source = stackNavigation,
        serializer = Config.serializer(),
        initialStack = { listOf(Config.List) },
        childFactory = ::createChild
    )

    private fun createChild(config: Config, componentContext: ComponentContext): ProChatFlowComponent.Child {
        return when (config) {
            Config.List -> ProChatFlowComponent.Child.List(
                component = DefaultProChatsListComponent(
                    componentContext = componentContext
                )
            )
            Config.Details -> ProChatFlowComponent.Child.Details(
                component = DefaultProChatDetailsComponent(
                    componentContext = componentContext,
                    dependencies = ProChatDetailsDependencies.Factory(dependencies),
                    showSnackBar = showSnackBar
                )
            )
        }
    }

    @Serializable
    private sealed class Config {
        @Serializable
        object List : Config()

        @Serializable
        object Details : Config()
    }
}