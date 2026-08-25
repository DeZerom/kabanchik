package ru.kabanchik.common.features.chat.logic.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kabanchik.features.common.chat.logic.generated.resources.Res
import kabanchik.features.common.chat.logic.generated.resources.chat_details_operator_found
import ru.kabanchik.common.feature.chat.model.CommonUiAttachment
import ru.kabanchik.common.feature.chat.model.CommonUiMessage
import ru.kabanchik.common.tools.extensions.getValue
import ru.kabanchik.common.tools.textResource.TextResource
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
                modifier = modifier,
                attachmentsContent = message.attachments
                    .takeIf { it.any(CommonUiAttachment::isImage) }
                    ?.let { attachments ->
                        {
                            CommonChatAttachments(attachments = attachments)
                        }
                    }
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

private fun CommonUiAttachment.isImage(): Boolean {
    return this is CommonUiAttachment.Image
}

@Preview
@Composable
private fun CommonChatUiDateMessagePreview() {
    CommonChatUiMessagePreviewContainer {
        CommonChatUiMessage(
            message = CommonChatUiMessageMock.date
        )
    }
}

@Preview
@Composable
private fun CommonChatUiOperatorFoundMessagePreview() {
    CommonChatUiMessagePreviewContainer {
        CommonChatUiMessage(
            message = CommonChatUiMessageMock.operatorFound
        )
    }
}

@Preview
@Composable
private fun CommonChatUiOperatorMessagePreview() {
    CommonChatUiMessagePreviewContainer {
        CommonChatUiMessage(
            message = CommonChatUiMessageMock.operatorMessage,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview
@Composable
private fun CommonChatUiUserMessagePreview() {
    CommonChatUiMessagePreviewContainer {
        CommonChatUiMessage(
            message = CommonChatUiMessageMock.userMessage,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun CommonChatUiMessagePreviewContainer(
    content: @Composable () -> Unit
) {
    KabanchikTheme {
        Surface(
            color = KabanchikTheme.colors.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                content()
            }
        }
    }
}

private object CommonChatUiMessageMock {
    val date = CommonUiMessage.Date(
        id = "date",
        date = "7 июля"
    )

    val operatorFound = CommonUiMessage.SystemMessage(
        id = "operator-found",
        message = TextResource.Id(Res.string.chat_details_operator_found)
    )

    val operatorMessage = CommonUiMessage.Message(
        id = "operator-message",
        authorLogin = "operator",
        isUserAuthor = false,
        time = "14:08",
        text = "Здравствуйте! Я подключился к чату и помогу с заказом."
    )

    val userMessage = CommonUiMessage.Message(
        id = "user-message",
        authorLogin = "client",
        isUserAuthor = true,
        time = "14:10",
        text = "Добрый день. Нужно уточнить детали по бронированию ресторана."
    )
}
