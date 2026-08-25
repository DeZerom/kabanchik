package ru.kabanchik.common.features.chat.logic.details

import kabanchik.features.common.chat.logic.generated.resources.Res
import kabanchik.features.common.chat.logic.generated.resources.chat_details_operator_found
import kabanchik.features.common.chat.logic.generated.resources.chat_details_session_end
import ru.kabanchik.common.chat.model.CommonAttachment
import ru.kabanchik.common.chat.model.CommonChatMessage
import ru.kabanchik.common.feature.chat.model.CommonUiAttachment
import ru.kabanchik.common.feature.chat.model.CommonUiMessage
import ru.kabanchik.common.tools.extensions.toDayFullMonth
import ru.kabanchik.common.tools.extensions.toHoursMinutes
import ru.kabanchik.common.tools.network.withBackendBaseUrl
import ru.kabanchik.common.tools.textResource.TextResource

fun CommonChatMessage.toState(userLogin: String): CommonUiMessage {
    return when (this) {
        is CommonChatMessage.Date -> {
            CommonUiMessage.Date(date = date.toDayFullMonth())
        }
        is CommonChatMessage.Message -> {
            CommonUiMessage.Message(
                id = message.id,
                authorLogin = message.authorLogin,
                isUserAuthor = message.isUserAuthor(userLogin),
                time = message.time.toHoursMinutes(),
                text = message.text,
                attachments = message.attachments.map { it.toState() },
            )
        }
        CommonChatMessage.OperatorFound -> {
            CommonUiMessage.SystemMessage(
                message = TextResource.Id(Res.string.chat_details_operator_found)
            )
        }
        CommonChatMessage.SessionEnd -> {
            CommonUiMessage.SystemMessage(
                message = TextResource.Id(Res.string.chat_details_session_end)
            )
        }
    }
}

private fun CommonAttachment.toState(): CommonUiAttachment {
    return if (contentType.startsWith(prefix = ImageMimeTypePrefix, ignoreCase = true)) {
        CommonUiAttachment.Image(
            fileId = fileId,
            originalName = originalName,
            contentType = contentType,
            size = size,
            downloadUrl = downloadUrl.withBackendBaseUrl(),
        )
    } else {
        CommonUiAttachment.File(
            fileId = fileId,
            originalName = originalName,
            contentType = contentType,
            size = size,
            downloadUrl = downloadUrl.withBackendBaseUrl(),
        )
    }
}

fun List<CommonUiMessage>.upsert(message: CommonUiMessage): List<CommonUiMessage> {
    if (message is CommonUiMessage.Date) {
        return if (any { it is CommonUiMessage.Date && it.date == message.date }) {
            this
        } else {
            this + message
        }
    }
    if (message !is CommonUiMessage.Message) return this + message

    val existingIndex = indexOfFirst { it.id == message.id }
    if (existingIndex == -1) return this + message

    return toMutableList().apply {
        this[existingIndex] = message
    }
}

private const val ImageMimeTypePrefix = "image/"
