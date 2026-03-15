package ru.kabanchik.common.features.chat.logic

import kabanchik.features.common.chat.logic.generated.resources.Res
import kabanchik.features.common.chat.logic.generated.resources.chat_details_operator_found
import kabanchik.features.common.chat.logic.generated.resources.chat_details_session_end
import ru.kabanchik.common.chat.model.CommonChatMessage
import ru.kabanchik.common.feature.chat.model.CommonUiMessage
import ru.kabanchik.common.tools.extensions.toDayFullMonth
import ru.kabanchik.common.tools.extensions.toHoursMinutes
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