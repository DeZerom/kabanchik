package ru.kabanchik.feature.client.chatDetails.internal

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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kabanchik.features.client.chat_details.generated.resources.Res
import kabanchik.features.client.chat_details.generated.resources.chat_details_hint
import org.jetbrains.compose.resources.stringResource
import ru.kabanchik.common.uiKit.HSpacer
import ru.kabanchik.common.uiKit.KabanchikIcons
import ru.kabanchik.common.uiKit.VSpacer
import ru.kabanchik.common.uiKit.theme.KabanchikTheme
import ru.kabanchik.common.uiKit.theme.cardDefault
import ru.kabanchik.common.uiKit.widgets.CommonButton
import ru.kabanchik.common.uiKit.widgets.CommonCircleButton
import ru.kabanchik.common.uiKit.widgets.CommonScreenLoader
import ru.kabanchik.common.uiKit.widgets.CommonTextInput
import ru.kabanchik.feature.client.chatDetails.api.ChatDetailsContract

@Composable
internal fun ChatDetailsContent(
    state: ChatDetailsContract.State,
    onLoginSelected: (String) -> Unit,
    onMessageTextChanged: (String) -> Unit,
    onMessageSent: () -> Unit,
) {
    Scaffold { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (state) {
                ChatDetailsContract.State.Loading -> {
                    CommonScreenLoader()
                }
                ChatDetailsContract.State.NoLogin -> {
                    SelectLogin(onLoginSelected)
                }
                is ChatDetailsContract.State.Chat -> {
                    Chat(
                        state = state,
                        onMessageTextChanged = onMessageTextChanged,
                        onMessageSent = onMessageSent
                    )
                }
            }
        }
    }
}

@Composable
private fun SelectLogin(
    onLoginSelected: (String) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        CommonButton(
            onClick = { onLoginSelected("Bob") },
            text = "Bob",
            modifier = Modifier.fillMaxWidth()
        )
        VSpacer(16.dp)
        CommonButton(
            onClick = { onLoginSelected("Alice") },
            text = "Alice",
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun Chat(
    state: ChatDetailsContract.State.Chat,
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
                contentPadding = PaddingValues(all = 16.dp),
                verticalArrangement = Arrangement.Bottom,
                state = listState,
                modifier = Modifier.weight(1f)
            ) {
                items(
                    items = state.messages,
                    key = { it.id }
                ) { message ->
                    VSpacer(16.dp)
                    MessageCard(
                        isUserAuthor = message.isUserAuthor,
                        messageDate = message.date,
                        messageTime = message.time,
                        messageText = message.text,
                    )
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
                        value = state.currentMessage,
                        onValueChange = onMessageTextChanged,
                        label = stringResource(Res.string.chat_details_hint),
                        modifier = Modifier.weight(1f)
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