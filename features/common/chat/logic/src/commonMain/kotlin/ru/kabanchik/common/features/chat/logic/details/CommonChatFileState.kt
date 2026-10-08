package ru.kabanchik.common.features.chat.logic.details

import ru.kabanchik.common.feature.chat.model.CommonUiAttachment
import ru.kabanchik.common.feature.chat.model.CommonUiMessage

fun List<CommonUiMessage>.findFile(fileId: String): CommonUiAttachment.File? {
    return asSequence()
        .filterIsInstance<CommonUiMessage.Message>()
        .flatMap { it.attachments.asSequence() }
        .filterIsInstance<CommonUiAttachment.File>()
        .firstOrNull { it.fileId == fileId }
}

fun List<CommonUiMessage>.withLoadingFile(fileId: String?): List<CommonUiMessage> {
    return map { message ->
        if (message !is CommonUiMessage.Message) return@map message

        message.copy(
            attachments = message.attachments.map { attachment ->
                if (attachment is CommonUiAttachment.File) {
                    attachment.copy(isLoading = attachment.fileId == fileId)
                } else {
                    attachment
                }
            }
        )
    }
}
