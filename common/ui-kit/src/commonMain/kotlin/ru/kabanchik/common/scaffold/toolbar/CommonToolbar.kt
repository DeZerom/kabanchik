package ru.kabanchik.common.scaffold.toolbar

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import ru.kabanchik.common.tools.extensions.getValue
import ru.kabanchik.common.tools.textResource.TextResource
import ru.kabanchik.common.uiKit.icons.KabanchikIcons
import ru.kabanchik.common.uiKit.icons.extensions.ArrowBack24
import ru.kabanchik.common.uiKit.theme.KabanchikTheme
import ru.kabanchik.common.uiKit.theme.bodyMedium
import ru.kabanchik.common.uiKit.theme.headlineLarge
import ru.kabanchik.common.uiKit.theme.smallTitle

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
        is CommonToolbarModel.BackButtonTitleSubtitle -> {
            BackButtonTitleSubtitleToolbar(toolbarModel, modifier)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BackButtonTitleSubtitleToolbar(
    model: CommonToolbarModel.BackButtonTitleSubtitle,
    modifier: Modifier
) {
    TopAppBar(
        title = { ToolbarTitleSubtitle(model.title, model.subtitle) },
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
        style = KabanchikTheme.typography.headlineLarge,
        color = KabanchikTheme.colors.mainText,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
private fun ToolbarTitleSubtitle(
    title: TextResource,
    subtitle: TextResource
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = title.getValue(),
            style = KabanchikTheme.typography.smallTitle,
            color = KabanchikTheme.colors.mainText,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = subtitle.getValue(),
            style = KabanchikTheme.typography.bodyMedium,
            color = KabanchikTheme.colors.secondaryText,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun toolbarColors(): TopAppBarColors {
    return TopAppBarDefaults.topAppBarColors(
        containerColor = KabanchikTheme.colors.background
    )
}
