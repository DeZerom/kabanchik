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
import androidx.compose.ui.graphics.Color
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
        title = { ToolbarTitle(model.title, model.style) },
        colors = toolbarColors(model.style),
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
        title = { ToolbarTitle(model.title, model.style) },
        navigationIcon = {
            IconButton(
                onClick = model.onBackClicked
            ) {
                Icon(
                    imageVector = KabanchikIcons.ArrowBack24,
                    contentDescription = null,
                    tint = model.style.navigationIconColor()
                )
            }
        },
        colors = toolbarColors(model.style),
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
        title = { ToolbarTitleSubtitle(model.title, model.subtitle, model.style) },
        navigationIcon = {
            IconButton(
                onClick = model.onBackClicked
            ) {
                Icon(
                    imageVector = KabanchikIcons.ArrowBack24,
                    contentDescription = null,
                    tint = model.style.navigationIconColor()
                )
            }
        },
        colors = toolbarColors(model.style),
        modifier = modifier
    )
}

@Composable
private fun ToolbarTitle(
    text: TextResource,
    style: CommonToolbarStyle,
) {
    Text(
        text = text.getValue(),
        style = KabanchikTheme.typography.headlineLarge,
        color = style.titleColor(),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
private fun ToolbarTitleSubtitle(
    title: TextResource,
    subtitle: TextResource,
    style: CommonToolbarStyle,
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = title.getValue(),
            style = KabanchikTheme.typography.smallTitle,
            color = style.titleColor(),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = subtitle.getValue(),
            style = KabanchikTheme.typography.bodyMedium,
            color = style.subtitleColor(),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun toolbarColors(style: CommonToolbarStyle): TopAppBarColors {
    return TopAppBarDefaults.topAppBarColors(
        containerColor = style.containerColor(),
        scrolledContainerColor = style.containerColor(),
    )
}

@Composable
private fun CommonToolbarStyle.containerColor(): Color {
    return when (this) {
        CommonToolbarStyle.Default -> KabanchikTheme.colors.background
        CommonToolbarStyle.Black -> Color.Black
    }
}

@Composable
private fun CommonToolbarStyle.navigationIconColor(): Color {
    return when (this) {
        CommonToolbarStyle.Default -> KabanchikTheme.colors.interactive
        CommonToolbarStyle.Black -> Color.White
    }
}

@Composable
private fun CommonToolbarStyle.titleColor(): Color {
    return when (this) {
        CommonToolbarStyle.Default -> KabanchikTheme.colors.mainText
        CommonToolbarStyle.Black -> Color.White
    }
}

@Composable
private fun CommonToolbarStyle.subtitleColor(): Color {
    return when (this) {
        CommonToolbarStyle.Default -> KabanchikTheme.colors.secondaryText
        CommonToolbarStyle.Black -> Color.White.copy(alpha = BlackSubtitleAlpha)
    }
}

private const val BlackSubtitleAlpha = 0.7f
