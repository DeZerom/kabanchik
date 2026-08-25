package ru.kabanchik.common.features.chat.logic.details

import kotlinx.datetime.LocalDateTime
import ru.kabanchik.common.chat.model.CommonAttachment
import ru.kabanchik.common.chat.model.CommonChatMessage
import ru.kabanchik.common.chat.model.CommonMessage
import ru.kabanchik.common.chat.model.CommonMessageType
import ru.kabanchik.common.feature.chat.model.CommonUiAttachment
import ru.kabanchik.common.feature.chat.model.CommonUiMessage
import ru.kabanchik.common.tools.network.BackendBaseUrl
import kotlin.test.Test
import kotlin.test.assertEquals

class CommonChatMessageMapperTest {
    @Test
    fun mapsEveryAttachmentAndClassifiesImagesByMimeType() {
        val attachments = listOf(
            attachment(fileId = "jpeg", contentType = "image/jpeg"),
            attachment(fileId = "pdf", contentType = "application/pdf"),
            attachment(fileId = "png", contentType = "IMAGE/PNG"),
            attachment(fileId = "unknown", contentType = ""),
        )

        val result = CommonChatMessage.Message(
            message = CommonMessage(
                id = "message-id",
                sessionId = "session-id",
                authorLogin = "client",
                text = "Message",
                type = CommonMessageType.Text,
                attachments = attachments,
                time = LocalDateTime(2026, 8, 25, 12, 0),
            )
        ).toState(userLogin = "client") as CommonUiMessage.Message

        assertEquals(
            listOf("jpeg", "pdf", "png", "unknown"),
            result.attachments.map { it.fileId }
        )
        assertEquals(
            listOf(
                CommonUiAttachment.Image::class,
                CommonUiAttachment.File::class,
                CommonUiAttachment.Image::class,
                CommonUiAttachment.File::class,
            ),
            result.attachments.map { it::class }
        )
    }

    @Test
    fun addsBackendBaseUrlToRelativeAttachmentUrl() {
        val attachment = attachment(
            fileId = "image",
            contentType = "image/jpeg",
            downloadUrl = "/chat/api/files/image",
        )

        val result = messageWith(attachment).attachments.single()

        assertEquals("$BackendBaseUrl/chat/api/files/image", result.downloadUrl)
    }

    @Test
    fun keepsAbsoluteAttachmentUrlUnchanged() {
        val absoluteUrl = "https://cdn.example.com/image"
        val attachment = attachment(
            fileId = "image",
            contentType = "image/jpeg",
            downloadUrl = absoluteUrl,
        )

        val result = messageWith(attachment).attachments.single()

        assertEquals(absoluteUrl, result.downloadUrl)
    }

    @Test
    fun backendMessageReplacesOptimisticMessageWithSameId() {
        val optimisticMessage = message(text = "Optimistic")
        val confirmedMessage = message(text = "Confirmed")

        val result = listOf(optimisticMessage).upsert(confirmedMessage)

        assertEquals(listOf(confirmedMessage), result)
    }

    @Test
    fun dateIsNotDuplicatedWhenBackendConfirmsLocalMessage() {
        val date = CommonUiMessage.Date(id = "date-1", date = "5 августа")
        val backendDate = CommonUiMessage.Date(id = "date-2", date = "5 августа")

        val result = listOf(date).upsert(backendDate)

        assertEquals(listOf(date), result)
    }

    private fun message(text: String): CommonUiMessage.Message = CommonUiMessage.Message(
        id = "message-id",
        authorLogin = "client",
        isUserAuthor = true,
        time = "12:00",
        text = text,
    )

    private fun messageWith(attachment: CommonAttachment): CommonUiMessage.Message {
        return CommonChatMessage.Message(
            message = CommonMessage(
                id = "message-id",
                sessionId = "session-id",
                authorLogin = "client",
                text = "Message",
                type = CommonMessageType.Text,
                attachments = listOf(attachment),
                time = LocalDateTime(2026, 8, 25, 12, 0),
            )
        ).toState(userLogin = "client") as CommonUiMessage.Message
    }

    private fun attachment(
        fileId: String,
        contentType: String,
        downloadUrl: String = "https://example.com/$fileId",
    ): CommonAttachment {
        return CommonAttachment(
            fileId = fileId,
            originalName = "$fileId.file",
            contentType = contentType,
            size = 1L,
            downloadUrl = downloadUrl,
        )
    }
}
