package ru.kabanchik.common.features.chat.logic.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kabanchik.features.common.chat.logic.generated.resources.Res
import kabanchik.features.common.chat.logic.generated.resources.chat_list_no_closed_chats
import kabanchik.features.common.chat.logic.generated.resources.chat_list_tab_active
import kabanchik.features.common.chat.logic.generated.resources.chat_list_tab_closed
import org.jetbrains.compose.resources.stringResource
import ru.kabanchik.common.feature.chat.model.CommonChatListTab
import ru.kabanchik.common.feature.chat.model.CommonUiChatItem
import ru.kabanchik.common.uiKit.theme.KabanchikTheme
import ru.kabanchik.common.uiKit.widgets.CommonScreenLoader
import ru.kabanchik.common.uiKit.widgets.CommonSegmentedTabs

/**
 * Список чатов с вкладками «Активные» и «Завершённые», переключаются тапом и свайпом.
 * Источник правды - [selectedTab]: пейджер только сообщает о свайпе через [onTabSelected].
 *
 * @param isClosedLoaded завершенные заявки уже загружались. До этого вместо пустого списка показывается лоадер,
 * иначе при свайпе мелькнет заглушка.
 * @param activeContent содержимое вкладки активных чатов.
 */
@Composable
fun CommonChatListTabs(
    selectedTab: CommonChatListTab,
    closedChats: List<CommonUiChatItem>,
    isClosedLoaded: Boolean,
    onTabSelected: (CommonChatListTab) -> Unit,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    activeContent: @Composable () -> Unit,
) {
    val tabs = CommonChatListTab.entries
    val pagerState = rememberPagerState(initialPage = selectedTab.ordinal) { tabs.size }
    val currentSelectedTab by rememberUpdatedState(selectedTab)
    val currentOnTabSelected by rememberUpdatedState(onTabSelected)

    // Вкладку выбрали тапом: докручиваем пейджер
    LaunchedEffect(selectedTab) {
        if (pagerState.settledPage != selectedTab.ordinal) {
            pagerState.animateScrollToPage(selectedTab.ordinal)
        }
    }
    // Вкладку выбрали свайпом: сообщаем, когда страница встала на место
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.settledPage }.collect { page ->
            if (tabs[page] != currentSelectedTab) {
                currentOnTabSelected(tabs[page])
            }
        }
    }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        CommonSegmentedTabs(
            tabs = listOf(
                stringResource(Res.string.chat_list_tab_active),
                stringResource(Res.string.chat_list_tab_closed),
            ),
            selectedIndex = pagerState.targetPage,
            onTabSelected = { index -> onTabSelected(tabs[index]) },
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp)
        )
        HorizontalPager(
            state = pagerState,
            // Обе страницы держим в композиции, чтобы не терять прокрутку списков
            beyondViewportPageCount = 1,
            key = { page -> tabs[page] },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) { page ->
            when (tabs[page]) {
                CommonChatListTab.Active -> activeContent()
                CommonChatListTab.Closed -> ClosedChats(
                    closedChats = closedChats,
                    isClosedLoaded = isClosedLoaded,
                    onItemClick = onItemClick,
                )
            }
        }
    }
}

@Composable
private fun ClosedChats(
    closedChats: List<CommonUiChatItem>,
    isClosedLoaded: Boolean,
    onItemClick: (String) -> Unit,
) {
    if (!isClosedLoaded) {
        CommonScreenLoader()
    } else {
        CommonChatListContent(
            items = closedChats,
            emptyListTitle = stringResource(Res.string.chat_list_no_closed_chats),
            emptyListMessage = null,
            emptyListButtonText = null,
            emptyListButtonLoading = false,
            onItemClick = onItemClick,
            onAddClick = {},
        )
    }
}

@Preview
@Composable
private fun CommonChatListTabsClosedPreview() {
    KabanchikTheme {
        Scaffold {
            CommonChatListTabs(
                selectedTab = CommonChatListTab.Closed,
                closedChats = listOf(ChatListMock.chatClosed),
                isClosedLoaded = true,
                onTabSelected = {},
                onItemClick = {},
                activeContent = {}
            )
        }
    }
}

@Preview
@Composable
private fun CommonChatListTabsClosedEmptyPreview() {
    KabanchikTheme {
        Scaffold {
            CommonChatListTabs(
                selectedTab = CommonChatListTab.Closed,
                closedChats = emptyList(),
                isClosedLoaded = true,
                onTabSelected = {},
                onItemClick = {},
                activeContent = {}
            )
        }
    }
}
