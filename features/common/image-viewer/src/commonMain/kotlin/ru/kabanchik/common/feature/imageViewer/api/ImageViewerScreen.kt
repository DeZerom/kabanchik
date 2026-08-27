package ru.kabanchik.common.feature.imageViewer.api

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import ru.kabanchik.common.feature.imageViewer.internal.ImageViewerContent
import ru.kabanchik.common.scaffold.toolbar.CommonToolbarModel
import ru.kabanchik.common.scaffold.toolbar.CommonToolbarStyle
import ru.kabanchik.common.tools.textResource.TextResource
import ru.kabanchik.common.uiKit.widgets.toolbar.AffectScaffold

@Composable
fun ImageViewerScreen(component: ImageViewerComponent) {
    val state by component.state.collectAsState()

    AffectScaffold(
        toolbar = CommonToolbarModel.BackButtonTitle(
            title = TextResource.Raw(""),
            onBackClicked = component::onBackClicked,
            style = CommonToolbarStyle.Black,
        )
    )

    ImageViewerContent(
        imageUrls = state.imageUrls,
        initialImageIndex = state.initialImageIndex,
    )
}
