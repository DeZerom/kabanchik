package ru.kabanchik.common.features.chat.logic.details

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.kabanchik.common.feature.chat.model.CommonUiMessage
import ru.kabanchik.common.tools.extensions.getValue
import ru.kabanchik.common.uiKit.theme.KabanchikTheme
import ru.kabanchik.common.uiKit.theme.extraSmallText
import ru.kabanchik.common.uiKit.widgets.CommonMessageCard

@Composable
fun CommonChatUiMessage(
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