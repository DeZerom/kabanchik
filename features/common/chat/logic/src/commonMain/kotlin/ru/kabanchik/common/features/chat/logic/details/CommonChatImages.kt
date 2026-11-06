package ru.kabanchik.common.features.chat.logic.details

import ru.kabanchik.common.feature.chat.model.CommonUiAttachment
import ru.kabanchik.common.feature.chat.model.CommonUiMessage

fun List<CommonUiMessage>.imageUrls(): List<String> {
    return asSequence()
        .filterIsInstance<CommonUiMessage.Message>()
        .flatMap { it.attachments.asSequence() }
        .filterIsInstance<CommonUiAttachment.Image>()
        .map(CommonUiAttachment.Image::downloadUrl)
        .toList()
}
