package ru.kabanchik.common.features.chat.logic.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.kabanchik.common.feature.chat.model.CommonUiChatItem
import ru.kabanchik.common.uiKit.icons.KabanchikIcons
import ru.kabanchik.common.uiKit.theme.KabanchikTheme

@Composable
fun CommonChatListContent(
    items: List<CommonUiChatItem>,
    emptyListMessage: String,
    emptyListButtonText: String,
    emptyListButtonLoading: Boolean,
    onItemClick: (String) -> Unit,
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier,
    listFabContentDescription: String? = null,
    listFabLoading: Boolean = false
) {
    if (items.isEmpty()) {
        CommonChatListEmptyStub(
            message = emptyListMessage,
            buttonText = emptyListButtonText,
            isLoading = emptyListButtonLoading,
            onButtonClick = onAddClick,
            modifier = modifier
        )
    } else {
        Box(
            modifier = modifier
                .padding(horizontal = 16.dp)
                .fillMaxSize()
        ) {
            LazyColumn(
                contentPadding = PaddingValues(
                    top = 24.dp,
                    bottom = if (listFabContentDescription != null) 96.dp else 24.dp
                ),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .fillMaxWidth()
            ) {
                items(
                    items = items,
                    key = { it.id }
                ) { chatItem ->
                    CommonChatCard(
                        item = chatItem,
                        onClick = { onItemClick(chatItem.id) }
                    )
                }
            }

            if (listFabContentDescription != null) {
                FloatingActionButton(
                    onClick = {
                        if (!listFabLoading) {
                            onAddClick()
                        }
                    },
                    containerColor = KabanchikTheme.colors.accent,
                    contentColor = KabanchikTheme.colors.mainText,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(bottom = 16.dp)
                ) {
                    if (listFabLoading) {
                        CircularProgressIndicator(
                            color = KabanchikTheme.colors.mainText,
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        Icon(
                            painter = KabanchikIcons.Plus24,
                            contentDescription = listFabContentDescription
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun CommonChatListContentEmptyPreview() {
    KabanchikTheme {
        Scaffold {
            CommonChatListContent(
                items = emptyList(),
                emptyListMessage = "Создайте первый чат и начните общение с оператором",
                emptyListButtonText = "Создать новый чат",
                emptyListButtonLoading = false,
                onItemClick = {},
                onAddClick = {}
            )
        }
    }
}

@Preview
@Composable
private fun CommonChatListContentListPreview() {
    KabanchikTheme {
        Scaffold {
            CommonChatListContent(
                items = ChatListMock.chatsList,
                emptyListMessage = "Создайте первый чат и начните общение с оператором",
                emptyListButtonText = "Создать новый чат",
                emptyListButtonLoading = false,
                onItemClick = {},
                onAddClick = {},
                listFabContentDescription = "Создать новый чат"
            )
        }
    }
}
