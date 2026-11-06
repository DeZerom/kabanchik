package ru.kabanchik.common.features.chat.logic.details

import kabanchik.features.common.chat.logic.generated.resources.Res
import kabanchik.features.common.chat.logic.generated.resources.chat_resolution_closed
import ru.kabanchik.common.chat.model.CommonChatResolution
import ru.kabanchik.common.tools.extensions.asTextResource
import ru.kabanchik.common.tools.textResource.TextResource

fun CommonChatResolution.toTextResource(): TextResource = when (this) {
    CommonChatResolution.Closed -> Res.string.chat_resolution_closed.asTextResource()
}
