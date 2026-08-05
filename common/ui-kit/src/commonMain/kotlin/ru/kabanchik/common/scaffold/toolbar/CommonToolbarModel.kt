package ru.kabanchik.common.scaffold.toolbar

import ru.kabanchik.common.tools.textResource.TextResource

sealed interface CommonToolbarModel {
    data class Title(val title: TextResource) : CommonToolbarModel
    data class BackButtonTitle(val title: TextResource, val onBackClicked: () -> Unit) : CommonToolbarModel
    data class BackButtonTitleSubtitle(
        val title: TextResource,
        val subtitle: TextResource,
        val onBackClicked: () -> Unit
    ) : CommonToolbarModel
}
