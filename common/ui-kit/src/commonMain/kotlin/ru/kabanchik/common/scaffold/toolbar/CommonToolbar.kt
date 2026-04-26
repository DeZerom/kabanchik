package ru.kabanchik.common.scaffold.toolbar

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.kabanchik.common.tools.extensions.getValue
import ru.kabanchik.common.tools.textResource.TextResource
import ru.kabanchik.common.uiKit.theme.KabanchikTheme
import ru.kabanchik.common.uiKit.theme.bigTitle

@Composable
fun CommonToolbar(
    toolbarModel: CommonToolbarModel?,
    modifier: Modifier = Modifier
) {
    when (toolbarModel) {
        is CommonToolbarModel.Title -> {
            TitleToolbar(toolbarModel, modifier)
        }
        null -> Unit
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TitleToolbar(
    model: CommonToolbarModel.Title,
    modifier: Modifier
) {
    TopAppBar(
        title = { ToolbarTitle(model.title) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = KabanchikTheme.colors.background
        )
    )
}

@Composable
private fun ToolbarTitle(text: TextResource) {
    Text(
        text = text.getValue(),
        style = KabanchikTheme.typography.bigTitle,
        color = KabanchikTheme.colors.mainText
    )
}