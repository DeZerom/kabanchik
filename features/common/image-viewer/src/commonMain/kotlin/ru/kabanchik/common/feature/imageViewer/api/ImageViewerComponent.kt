package ru.kabanchik.common.feature.imageViewer.api

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.StateFlow
import ru.kabanchik.common.feature.imageViewer.internal.DefaultImageViewerComponent

interface ImageViewerComponent {
    val state: StateFlow<ImageViewerContract.State>

    fun onBackClicked()

    companion object {
        fun create(
            componentContext: ComponentContext,
            imageUrls: List<String>,
            selectedImageUrl: String,
            navigateBack: () -> Unit,
        ): ImageViewerComponent {
            return DefaultImageViewerComponent(
                componentContext = componentContext,
                imageUrls = imageUrls,
                selectedImageUrl = selectedImageUrl,
                navigateBack = navigateBack,
            )
        }
    }
}
