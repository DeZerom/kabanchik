package ru.kabanchik.common.scaffold.toolbar

import ru.kabanchik.common.tools.textResource.TextResource

sealed interface CommonToolbarModel {
    data class Title(val title: TextResource) : CommonToolbarModel
}