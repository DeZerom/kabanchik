package ru.kabanchik.common.features.chat.logic.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kabanchik.features.common.chat.logic.generated.resources.Res
import kabanchik.features.common.chat.logic.generated.resources.chat_list_no_chats
import org.jetbrains.compose.resources.stringResource
import ru.kabanchik.common.uiKit.VSpacer
import ru.kabanchik.common.uiKit.icons.KabanchikIcons
import ru.kabanchik.common.uiKit.icons.extensions.Forum96
import ru.kabanchik.common.uiKit.theme.KabanchikTheme
import ru.kabanchik.common.uiKit.theme.bodyMedium
import ru.kabanchik.common.uiKit.theme.headlineSmall
import ru.kabanchik.common.uiKit.widgets.CommonButton

@Composable
internal fun CommonChatListEmptyStub(
    message: String,
    buttonText: String,
    isLoading: Boolean,
    onButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .padding(all = 16.dp)
            .fillMaxSize(),
    ) {
        Icon(
            imageVector = KabanchikIcons.Forum96,
            contentDescription = null,
            tint = KabanchikTheme.colors.accent
        )
        VSpacer(24.dp)
        Text(
            text = stringResource(Res.string.chat_list_no_chats),
            style = KabanchikTheme.typography.headlineSmall,
            color = KabanchikTheme.colors.mainText
        )
        VSpacer(16.dp)
        Text(
            text = message,
            style = KabanchikTheme.typography.bodyMedium,
            color = KabanchikTheme.colors.secondaryText,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        VSpacer(36.dp)
        CommonButton(
            text = buttonText,
            isLoading = isLoading,
            onClick = onButtonClick,
            modifier = Modifier
        )
    }
}

@Preview
@Composable
private fun CommonChatListEmptyStubPreview() {
    KabanchikTheme {
        Scaffold {
            CommonChatListEmptyStub(
                message = "Создайте первый чат и начните общение с оператором",
                buttonText = "Создать новый чат",
                isLoading = false,
                onButtonClick = {}
            )
        }
    }
}

@Preview
@Composable
private fun CommonChatListEmptyStubLoadingPreview() {
    KabanchikTheme {
        Scaffold {
            CommonChatListEmptyStub(
                message = "Создайте первый чат и начните общение с оператором",
                buttonText = "Создать новый чат",
                isLoading = true,
                onButtonClick = {}
            )
        }
    }
}