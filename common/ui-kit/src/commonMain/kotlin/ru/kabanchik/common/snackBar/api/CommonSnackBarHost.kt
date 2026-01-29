package ru.kabanchik.common.snackBar.api

import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import ru.kabanchik.common.uiKit.widgets.CommonSnackBar

@Composable
fun CommonSnackBarHost(
    hostState: SnackbarHostState,
) {
    SnackbarHost(
        hostState = hostState,
        snackbar = { data ->
            when (val visuals = data.visuals) {
                is CommonVisuals -> {
                    CommonSnackBar(commonVisuals = visuals)
                }
                else -> Snackbar(data)
            }
        }
    )
}