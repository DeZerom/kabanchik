package ru.kabanchik.common.feature.imageViewer.internal

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ru.kabanchik.common.feature.imageViewer.api.ImageViewerComponent
import ru.kabanchik.common.feature.imageViewer.api.ImageViewerContract.State

internal class DefaultImageViewerComponent(
    componentContext: ComponentContext,
    imageUrls: List<String>,
    selectedImageUrl: String,
    private val navigateBack: () -> Unit,
) : ImageViewerComponent, ComponentContext by componentContext {
    override val state: StateFlow<State> = MutableStateFlow(
        State(
            imageUrls = imageUrls,
            initialImageIndex = imageUrls.indexOf(selectedImageUrl).takeIf { it >= 0 } ?: 0,
        )
    )

    override fun onBackClicked() {
        navigateBack()
    }
}
