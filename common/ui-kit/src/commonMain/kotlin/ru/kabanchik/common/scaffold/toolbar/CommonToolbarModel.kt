package ru.kabanchik.common.scaffold.toolbar

import ru.kabanchik.common.tools.textResource.TextResource

sealed interface CommonToolbarModel {
    val style: CommonToolbarStyle

    data class Title(
        val title: TextResource,
        override val style: CommonToolbarStyle = CommonToolbarStyle.Default,
    ) : CommonToolbarModel

    data class BackButtonTitle(
        val title: TextResource,
        val onBackClicked: () -> Unit,
        override val style: CommonToolbarStyle = CommonToolbarStyle.Default,
    ) : CommonToolbarModel

    data class BackButtonTitleSubtitle(
        val title: TextResource,
        val subtitle: TextResource,
        val onBackClicked: () -> Unit,
        override val style: CommonToolbarStyle = CommonToolbarStyle.Default,
    ) : CommonToolbarModel
}

enum class CommonToolbarStyle {
    Default,
    Black,
}
