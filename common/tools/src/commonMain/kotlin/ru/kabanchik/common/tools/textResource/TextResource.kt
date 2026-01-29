package ru.kabanchik.common.tools.textResource

import org.jetbrains.compose.resources.StringResource

sealed interface TextResource {
    class Raw(val value: String) : TextResource
    class Id(val value: StringResource) : TextResource
}