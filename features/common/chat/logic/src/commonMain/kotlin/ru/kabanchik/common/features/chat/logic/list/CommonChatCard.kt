package ru.kabanchik.common.features.chat.logic.list

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import kabanchik.features.common.chat.logic.generated.resources.Res
import kabanchik.features.common.chat.logic.generated.resources.chat_details_resolution_title
import kabanchik.features.common.chat.logic.generated.resources.chat_list_new_chat
import org.jetbrains.compose.resources.stringResource
import ru.kabanchik.common.feature.chat.model.CommonUiChatItem
import ru.kabanchik.common.tools.extensions.getValue
import ru.kabanchik.common.uiKit.HSpacer
import ru.kabanchik.common.uiKit.VSpacer
import ru.kabanchik.common.uiKit.theme.KabanchikTheme
import ru.kabanchik.common.uiKit.theme.bodyMedium
import ru.kabanchik.common.uiKit.theme.bodyS
import ru.kabanchik.common.uiKit.theme.cardDefault
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
                    text = item.title.ifEmpty { stringResource(Res.string.chat_list_new_chat) },
                    style = KabanchikTheme.typography.smallTitle,
                    color = KabanchikTheme.colors.mainText,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                HSpacer(4.dp)
                Text(
                    text = item.lastUpdate.getValue(),
                    style = KabanchikTheme.typography.bodyS,
                    color = KabanchikTheme.colors.secondaryText,
                )
            }
            if (item.otherPersonName != null) {
                VSpacer(4.dp)
                Text(
                    text = item.otherPersonName.orEmpty(),
                    style = KabanchikTheme.typography.bodyMedium,
                    color = KabanchikTheme.colors.secondaryText
                )
            }
            VSpacer(8.dp)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (item.resolution != null) {
                        stringResource(Res.string.chat_details_resolution_title)
                    } else {
                        item.lastMessage
                    },
                    style = KabanchikTheme.typography.bodyMedium,
                    color = KabanchikTheme.colors.secondaryText,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                item.resolution?.let { resolution ->
                    HSpacer(4.dp)
                    ResolutionBadge(text = resolution.getValue())
                }
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

@Composable
private fun ResolutionBadge(text: String) {
    Text(
        text = text,
        style = KabanchikTheme.typography.bodyS,
        color = KabanchikTheme.colors.accent,
        modifier = Modifier
            .border(width = 1.dp, color = KabanchikTheme.colors.accent, shape = CircleShape)
            .padding(horizontal = 12.dp, vertical = 2.dp)
    )
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
private fun CommonChatCardClosedPreview() {
    KabanchikTheme {
        CommonChatCard(
            item = ChatListMock.chatClosed,
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
