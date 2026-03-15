package ru.kabanchik.feature.client.chatDetails.internal.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kabanchik.features.client.chat.generated.resources.Res
import kabanchik.features.client.chat.generated.resources.chat_details_hint
import org.jetbrains.compose.resources.stringResource
import ru.kabanchik.common.feature.chat.model.CommonUiMessage
import ru.kabanchik.common.modifier.sendMessageModifier
import ru.kabanchik.common.tools.extensions.getValue
import ru.kabanchik.common.uiKit.HSpacer
import ru.kabanchik.common.uiKit.KabanchikIcons
import ru.kabanchik.common.uiKit.VSpacer
import ru.kabanchik.common.uiKit.theme.KabanchikTheme
import ru.kabanchik.common.uiKit.theme.cardDefault
import ru.kabanchik.common.uiKit.theme.extraSmallText
import ru.kabanchik.common.uiKit.widgets.CommonCircleButton
import ru.kabanchik.common.uiKit.widgets.CommonMessageCard
import ru.kabanchik.common.uiKit.widgets.CommonScreenLoader
import ru.kabanchik.common.uiKit.widgets.CommonTextInput
import ru.kabanchik.feature.client.chatDetails.api.details.ChatDetailsContract

@Composable
internal fun ChatDetailsContent(
    state: ChatDetailsContract.State,
    onMessageTextChanged: (String) -> Unit,
    onMessageSent: () -> Unit,
) {
    if (state.isLoading) {
        CommonScreenLoader()
    } else {
        Chat(
            state = state,
            onMessageTextChanged = onMessageTextChanged,
            onMessageSent = onMessageSent
        )
    }
}

@Composable
private fun Chat(
    state: ChatDetailsContract.State,
    onMessageTextChanged: (String) -> Unit,
    onMessageSent: () -> Unit
) {
    val listState = rememberLazyListState()
    LaunchedEffect(state.messages.size) {
        val layoutInfo = listState.layoutInfo
        val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()

        val wasAtBottom = lastVisibleItem == null ||
                lastVisibleItem.index >= layoutInfo.totalItemsCount - 2

        if (wasAtBottom && state.messages.isNotEmpty()) {
            listState.animateScrollToItem(state.messages.size - 1)
        }
    }

    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(bottom = 16.dp)
        ) {
            LazyColumn(
                contentPadding = PaddingValues(vertical = 16.dp),
                verticalArrangement = Arrangement.Bottom,
                state = listState,
                modifier = Modifier.weight(1f)
            ) {
                items(
                    items = state.messages,
                    key = { it.id }
                ) { message ->
                    VSpacer(16.dp)
                    Message(
                        message = message
                    )
                }
            }
            Surface(
                color = KabanchikTheme.colors.card,
                shape = KabanchikTheme.shapes.cardDefault,
                modifier = Modifier.navigationBarsPadding()
            ) {
                Row(
                    verticalAlignment = Alignment.Bottom,
                    modifier = Modifier.padding(all = 16.dp)
                ) {
                    CommonTextInput(
                        value = state.currentMessage,
                        onValueChange = onMessageTextChanged,
                        label = stringResource(Res.string.chat_details_hint),
                        modifier = Modifier
                            .weight(1f)
                            .sendMessageModifier(onMessageSent)
                    )
                    HSpacer(12.dp)
                    CommonCircleButton(
                        onClick = onMessageSent,
                        color = KabanchikTheme.colors.accent,
                        painter = KabanchikIcons.Send24,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun Message(
    message: CommonUiMessage,
    modifier: Modifier = Modifier
) {
    when (message) {
        is CommonUiMessage.Date -> {
            Text(
                text = message.date,
                style = KabanchikTheme.typography.extraSmallText,
                color = KabanchikTheme.colors.secondaryText,
                modifier = modifier
            )
        }
        is CommonUiMessage.Message -> {
            CommonMessageCard(
                isUserAuthor = message.isUserAuthor,
                messageTime = message.time,
                messageText = message.text,
                authorLogin = message.authorLogin,
                modifier = modifier
            )
        }
        is CommonUiMessage.SystemMessage -> {
            Text(
                text = message.message.getValue(),
                style = KabanchikTheme.typography.extraSmallText,
                color = KabanchikTheme.colors.secondaryText,
                modifier = modifier
            )
        }
    }
}