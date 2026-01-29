package ru.kabanchik.common.snackBar.api

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarVisuals
import ru.kabanchik.common.tools.textResource.TextResource

data class CommonVisuals(
    val type: CommonVisualsType,
    val textResource: TextResource
) : SnackbarVisuals {
    override val actionLabel: String? = null
    override val duration: SnackbarDuration = SnackbarDuration.Short
    override val message: String = ""
    override val withDismissAction: Boolean = false
}

enum class CommonVisualsType {
    Error, Success
}