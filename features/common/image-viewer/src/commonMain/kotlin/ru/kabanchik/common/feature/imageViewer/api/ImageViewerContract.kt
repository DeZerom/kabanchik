package ru.kabanchik.common.feature.imageViewer.api

class ImageViewerContract {
    data class State(
        val imageUrls: List<String> = emptyList(),
        val initialImageIndex: Int = 0,
    )
}
