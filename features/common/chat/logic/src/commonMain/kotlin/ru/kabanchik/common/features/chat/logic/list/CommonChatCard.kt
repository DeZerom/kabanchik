package ru.kabanchik.common.features.chat.logic.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.kabanchik.common.feature.chat.model.CommonUiChatItem
import ru.kabanchik.common.uiKit.HSpacer
import ru.kabanchik.common.uiKit.VSpacer
import ru.kabanchik.common.uiKit.theme.KabanchikTheme
import ru.kabanchik.common.uiKit.theme.cardDefault
import ru.kabanchik.common.uiKit.theme.regularText
import ru.kabanchik.common.uiKit.theme.smallText
import ru.kabanchik.common.uiKit.theme.smallTitle

@Composable
internal fun CommonChatCard(
    item: CommonUiChatItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = KabanchikTheme.colors.card,
        shape = KabanchikTheme.shapes.cardDefault,
        onClick = onClick,
        modifier = modifier
    ) {
        Column(
            Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = item.title,
                    style = KabanchikTheme.typography.smallTitle,
                    color = KabanchikTheme.colors.mainText,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                HSpacer(4.dp)
                Text(
                    text = item.lastUpdate,
                    style = KabanchikTheme.typography.smallText,
                    color = KabanchikTheme.colors.secondaryText,
                )
            }
            if (item.otherPersonName != null) {
                VSpacer(4.dp)
                Text(
                    text = item.otherPersonName.orEmpty(),
                    style = KabanchikTheme.typography.regularText,
                    color = KabanchikTheme.colors.secondaryText
                )
            }
            VSpacer(8.dp)
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = item.lastMessage,
                    style = KabanchikTheme.typography.regularText,
                    color = KabanchikTheme.colors.secondaryText,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                if (item.hasUnread) {
                    HSpacer(4.dp)
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(KabanchikTheme.colors.accent)
                            .size(16.dp)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun CommonChatCardPreview() {
    KabanchikTheme {
        CommonChatCard(
            item = ChatListMock.chatWithUnread,
            onClick = {}
        )
    }
}

@Preview
@Composable
private fun CommonChatCardLongTitlePreview() {
    KabanchikTheme {
        CommonChatCard(
            item = ChatListMock.chatLongFields,
            onClick = {}
        )
    }
}

@Preview
@Composable
private fun CommonChatCardNoNamePreview() {
    KabanchikTheme {
        CommonChatCard(
            item = ChatListMock.chatNoName,
            onClick = {}
        )
    }
}

@Preview
@Composable
private fun CommonChatCardDatePreview() {
    KabanchikTheme {
        CommonChatCard(
            item = ChatListMock.chatDate,
            onClick = {}
        )
    }
}