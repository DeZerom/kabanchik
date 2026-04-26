package ru.kabanchik.common.uiKit.widgets.toolbar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import ru.kabanchik.common.scaffold.ScaffoldState
import ru.kabanchik.common.scaffold.toolbar.CommonToolbarModel

@Composable
fun AffectScaffold(
    toolbar: CommonToolbarModel? = null
) {
    LaunchedEffect(toolbar) {
        ScaffoldState.setToolbar(toolbar)
    }
}