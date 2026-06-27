package ru.kabanchik.common.features.chat.logic.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kabanchik.features.common.chat.logic.generated.resources.Res
import kabanchik.features.common.chat.logic.generated.resources.chat_details_hint
import org.jetbrains.compose.resources.stringResource
import ru.kabanchik.common.feature.chat.model.CommonUiMessage
import ru.kabanchik.common.modifier.sendMessageModifier
import ru.kabanchik.common.uiKit.HSpacer
import ru.kabanchik.common.uiKit.icons.KabanchikIcons
import ru.kabanchik.common.uiKit.theme.KabanchikTheme
import ru.kabanchik.common.uiKit.theme.cardDefault
import ru.kabanchik.common.uiKit.widgets.CommonCircleButton
import ru.kabanchik.common.uiKit.widgets.CommonTextInput

@Composable
fun CommonChatContent(
    messages: List<CommonUiMessage>,
    currentMessageText: String,
    onMessageTextChanged: (String) -> Unit,
    onMessageSent: () -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    val initialScrollDone = remember { mutableStateOf(false) }

    LaunchedEffect(messages.size) {
        if (messages.isEmpty()) {
            initialScrollDone.value = false
            return@LaunchedEffect
        }

        val lastMessageIndex = messages.lastIndex

        if (!initialScrollDone.value) {
            listState.scrollToItem(lastMessageIndex)
            initialScrollDone.value = true
            return@LaunchedEffect
        }

        val layoutInfo = listState.layoutInfo
        val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()
        val wasAtBottom = lastVisibleItem == null ||
                lastVisibleItem.index >= layoutInfo.totalItemsCount - 2

        if (wasAtBottom) {
            listState.animateScrollToItem(lastMessageIndex)
        }
    }

    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(bottom = 16.dp)
        ) {
            LazyColumn(
                contentPadding = PaddingValues(vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Bottom),
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                items(
                    items = messages,
                    key = { it.id }
                ) { message ->
                    CommonChatUiMessage(message)
                }
            }
            Surface(
                color = KabanchikTheme.colors.card,
                shape = KabanchikTheme.shapes.cardDefault,
            ) {
                Row(
                    verticalAlignment = Alignment.Bottom,
                    modifier = Modifier.padding(all = 16.dp)
                ) {
                    CommonTextInput(
                        value = currentMessageText,
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
