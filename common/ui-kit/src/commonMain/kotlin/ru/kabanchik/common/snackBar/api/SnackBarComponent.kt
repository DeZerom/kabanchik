package ru.kabanchik.common.snackBar.api

import androidx.compose.material3.SnackbarHostState
import com.arkivanov.decompose.ComponentContext
import ru.kabanchik.common.snackBar.internal.DefaultSnackBarComponent

interface SnackBarComponent {
    val hostState: SnackbarHostState

    fun setData(data: SnackBarData)

    companion object {
        fun create(componentContext: ComponentContext): SnackBarComponent {
            return DefaultSnackBarComponent(componentContext)
        }
    }
}