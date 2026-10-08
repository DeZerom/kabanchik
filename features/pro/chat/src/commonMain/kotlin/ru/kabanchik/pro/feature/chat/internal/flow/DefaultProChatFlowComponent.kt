package ru.kabanchik.pro.feature.chat.internal.flow

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.value.Value
import kotlinx.serialization.Serializable
import ru.kabanchik.common.feature.imageViewer.api.ImageViewerComponent
import ru.kabanchik.common.snackBar.api.SnackBarData
import ru.kabanchik.pro.feature.chat.api.details.ProChatDetailsDependencies
import ru.kabanchik.pro.feature.chat.api.flow.ProChatFlowComponent
import ru.kabanchik.pro.feature.chat.api.flow.ProChatFlowDependencies
import ru.kabanchik.pro.feature.chat.api.list.ProChatsListDependencies
import ru.kabanchik.pro.feature.chat.internal.details.DefaultProChatDetailsComponent
import ru.kabanchik.pro.feature.chat.internal.list.DefaultProChatsListComponent

class DefaultProChatFlowComponent(
    componentContext: ComponentContext,
    private val showSnackBar: (SnackBarData) -> Unit,
    private val dependencies: ProChatFlowDependencies
) : ProChatFlowComponent, ComponentContext by componentContext {
    private val stackNavigation = StackNavigation<Config>()
    override val stack: Value<ChildStack<*, ProChatFlowComponent.Child>> = childStack(
        source = stackNavigation,
        serializer = Config.serializer(),
        initialStack = { listOf(Config.ChatsList) },
        childFactory = ::createChild,
        handleBackButton = true
    )

    private fun createChild(config: Config, componentContext: ComponentContext): ProChatFlowComponent.Child {
        return when (config) {
            Config.ChatsList -> ProChatFlowComponent.Child.List(
                component = DefaultProChatsListComponent(
                    componentContext = componentContext,
                    dependencies = ProChatsListDependencies.Factory(dependencies),
                    showSnackBar = showSnackBar,
                    navigateDetails = { sessionId, shouldReconnect ->
                        stackNavigation.pushNew(Config.Details(sessionId, shouldReconnect))
                    }
                )
            )
            is Config.Details -> ProChatFlowComponent.Child.Details(
                component = DefaultProChatDetailsComponent(
                    componentContext = componentContext,
                    dependencies = ProChatDetailsDependencies.Factory(dependencies),
                    showSnackBar = showSnackBar,
                    navigateBack = { stackNavigation.pop() },
                    navigateImageViewer = { imageUrls, selectedImageUrl ->
                        stackNavigation.pushNew(
                            Config.ImageViewer(
                                imageUrls = imageUrls,
                                selectedImageUrl = selectedImageUrl,
                            )
                        )
                    },
                    sessionId = config.sessionId,
                    shouldReconnect = config.shouldReconnect
                )
            )
            is Config.ImageViewer -> ProChatFlowComponent.Child.ImageViewer(
                component = ImageViewerComponent.create(
                    componentContext = componentContext,
                    imageUrls = config.imageUrls,
                    selectedImageUrl = config.selectedImageUrl,
                    navigateBack = { stackNavigation.pop() },
                )
            )
        }
    }

    @Serializable
    private sealed class Config {
        @Serializable
        object ChatsList : Config()

        @Serializable
        data class Details(
            val sessionId: String,
            val shouldReconnect: Boolean
        ) : Config()

        @Serializable
        data class ImageViewer(
            val imageUrls: List<String>,
            val selectedImageUrl: String,
        ) : Config()
    }
}
