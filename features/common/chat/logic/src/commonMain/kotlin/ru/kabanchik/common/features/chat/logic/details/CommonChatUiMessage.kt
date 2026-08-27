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
    onFileClicked: (String) -> Unit = {},
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
                    .takeIf { it.isNotEmpty() }
                    ?.let { attachments ->
                        {
                            CommonChatAttachments(
                                attachments = attachments,
                                onFileClicked = onFileClicked,
                            )
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

@Preview
@Composable
private fun CommonChatUiFileMessagePreview() {
    CommonChatUiMessagePreviewContainer {
        CommonChatUiMessage(
            message = CommonChatUiMessageMock.fileMessage,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Preview
@Composable
private fun CommonChatUiMixedAttachmentsMessagePreview() {
    CommonChatUiMessagePreviewContainer {
        CommonChatUiMessage(
            message = CommonChatUiMessageMock.mixedAttachmentsMessage,
            modifier = Modifier.fillMaxWidth(),
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

    val fileMessage = CommonUiMessage.Message(
        id = "file-message",
        authorLogin = "client",
        isUserAuthor = true,
        time = "14:11",
        text = "Прикладываю документы",
        attachments = listOf(
            fileAttachment(
                id = "contract",
                name = "Договор на оказание услуг.pdf",
                size = 3_270_246,
            ),
            fileAttachment(
                id = "conditions",
                name = "Условия.docx",
                size = 92_160,
            ),
        ),
    )

    val mixedAttachmentsMessage = CommonUiMessage.Message(
        id = "mixed-attachments-message",
        authorLogin = "operator",
        isUserAuthor = false,
        time = "14:12",
        text = "Фото и документы по заказу",
        attachments = listOf(
            CommonUiAttachment.File(
                fileId = "estimate",
                originalName = "Смета.xlsx",
                contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                size = 184_320,
                downloadUrl = "https://example.com/estimate.xlsx",
            ),
            CommonUiAttachment.Image(
                fileId = "photo",
                originalName = "Фото.jpg",
                contentType = "image/jpeg",
                size = 512_000,
                downloadUrl = "https://example.com/photo.jpg",
            ),
        ),
    )

    private fun fileAttachment(
        id: String,
        name: String,
        size: Long,
    ): CommonUiAttachment.File {
        return CommonUiAttachment.File(
            fileId = id,
            originalName = name,
            contentType = "application/octet-stream",
            size = size,
            downloadUrl = "https://example.com/$id",
        )
    }
}
