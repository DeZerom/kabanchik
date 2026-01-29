package ru.kabanchik.common.snackBar.internal

import androidx.compose.material3.SnackbarHostState
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import kotlinx.coroutines.launch
import ru.kabanchik.common.snackBar.api.CommonVisuals
import ru.kabanchik.common.snackBar.api.CommonVisualsType
import ru.kabanchik.common.snackBar.api.SnackBarComponent
import ru.kabanchik.common.snackBar.api.SnackBarData

internal class DefaultSnackBarComponent(
    componentContext: ComponentContext
) : SnackBarComponent, ComponentContext by componentContext {
    override val hostState: SnackbarHostState = SnackbarHostState()

    private val coroutineScope = coroutineScope()

    override fun setData(data: SnackBarData) {
        coroutineScope.launch {
            when (data) {
                is SnackBarData.Error -> {
                    hostState.showSnackbar(
                        visuals = CommonVisuals(
                            type = CommonVisualsType.Error,
                            textResource = data.text
                        )
                    )
                }
            }
        }
    }
}