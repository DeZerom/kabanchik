package ru.kabanchik.common.tools.extensions

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.stringResource
import ru.kabanchik.common.tools.textResource.TextResource

@Composable
fun TextResource.getValue(): String {
    return when (this) {
        is TextResource.Id -> stringResource(value)
        is TextResource.Raw -> value
    }
}