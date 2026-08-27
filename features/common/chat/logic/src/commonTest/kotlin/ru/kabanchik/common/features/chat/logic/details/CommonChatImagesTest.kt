package ru.kabanchik.common.features.chat.logic.details

import ru.kabanchik.common.feature.chat.model.CommonUiAttachment
import ru.kabanchik.common.feature.chat.model.CommonUiMessage
import kotlin.test.Test
import kotlin.test.assertEquals

class CommonChatImagesTest {
    @Test
    fun extractsOnlyImageUrlsInMessageAndAttachmentOrder() {
        val messages = listOf(
            CommonUiMessage.Date(date = "today"),
            CommonUiMessage.Message(
                attachments = listOf(
                    image(url = "first"),
                    file(url = "file"),
                    image(url = "second"),
                )
            ),
            CommonUiMessage.Message(
                attachments = listOf(image(url = "third"))
            ),
        )

        assertEquals(
            expected = listOf("first", "second", "third"),
            actual = messages.imageUrls(),
        )
    }

    private fun image(url: String): CommonUiAttachment.Image {
        return CommonUiAttachment.Image(
            fileId = url,
            originalName = url,
            contentType = "image/jpeg",
            size = 1,
            downloadUrl = url,
        )
    }

    private fun file(url: String): CommonUiAttachment.File {
        return CommonUiAttachment.File(
            fileId = url,
            originalName = url,
            contentType = "application/pdf",
            size = 1,
            downloadUrl = url,
        )
    }
}
