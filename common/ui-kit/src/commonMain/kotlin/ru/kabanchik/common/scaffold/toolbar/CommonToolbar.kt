package ru.kabanchik.common.scaffold.toolbar

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.kabanchik.common.tools.extensions.getValue
import ru.kabanchik.common.tools.textResource.TextResource
import ru.kabanchik.common.uiKit.icons.KabanchikIcons
import ru.kabanchik.common.uiKit.icons.extensions.ArrowBack24
import ru.kabanchik.common.uiKit.theme.KabanchikTheme
import ru.kabanchik.common.uiKit.theme.bigHeadline

@Composable
fun CommonToolbar(
    toolbarModel: CommonToolbarModel?,
    modifier: Modifier = Modifier
) {
    when (toolbarModel) {
        is CommonToolbarModel.Title -> {
            TitleToolbar(toolbarModel, modifier)
        }
        is CommonToolbarModel.BackButtonTitle -> {
            BackButtonTitleToolbar(toolbarModel, modifier)
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
        colors = toolbarColors(),
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BackButtonTitleToolbar(
    model: CommonToolbarModel.BackButtonTitle,
    modifier: Modifier
) {
    TopAppBar(
        title = { ToolbarTitle(model.title) },
        navigationIcon = {
            IconButton(
                onClick = model.onBackClicked
            ) {
                Icon(
                    imageVector = KabanchikIcons.ArrowBack24,
                    contentDescription = null,
                    tint = KabanchikTheme.colors.interactive
                )
            }
        },
        colors = toolbarColors(),
        modifier = modifier
    )
}

@Composable
private fun ToolbarTitle(text: TextResource) {
    Text(
        text = text.getValue(),
        style = KabanchikTheme.typography.bigHeadline,
        color = KabanchikTheme.colors.mainText,
    )
}

@Composable
private fun toolbarColors(): TopAppBarColors {
    return TopAppBarDefaults.topAppBarColors(
        containerColor = KabanchikTheme.colors.background
    )
}