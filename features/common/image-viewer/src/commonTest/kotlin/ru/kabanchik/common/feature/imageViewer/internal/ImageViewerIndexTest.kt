package ru.kabanchik.common.feature.imageViewer.internal

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import kotlin.test.Test
import kotlin.test.assertEquals

class ImageViewerIndexTest {
    @Test
    fun returnsSelectedImageIndex() {
        assertEquals(
            expected = 1,
            actual = component(
                imageUrls = listOf("first", "selected", "last"),
                selectedImageUrl = "selected",
            ).state.value.initialImageIndex,
        )
    }

    @Test
    fun returnsFirstIndexWhenSelectedImageIsMissing() {
        assertEquals(
            expected = 0,
            actual = component(
                imageUrls = listOf("first", "last"),
                selectedImageUrl = "missing",
            ).state.value.initialImageIndex,
        )
    }

    @Test
    fun returnsFirstIndexForEmptyList() {
        assertEquals(
            expected = 0,
            actual = component(
                imageUrls = emptyList(),
                selectedImageUrl = "missing",
            ).state.value.initialImageIndex,
        )
    }

    private fun component(
        imageUrls: List<String>,
        selectedImageUrl: String,
    ): DefaultImageViewerComponent {
        return DefaultImageViewerComponent(
            componentContext = DefaultComponentContext(LifecycleRegistry()),
            imageUrls = imageUrls,
            selectedImageUrl = selectedImageUrl,
            navigateBack = {},
        )
    }
}
