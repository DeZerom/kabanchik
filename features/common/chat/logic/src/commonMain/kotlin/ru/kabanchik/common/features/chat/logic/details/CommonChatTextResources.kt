package ru.kabanchik.common.features.chat.logic.details

import kabanchik.features.common.chat.logic.generated.resources.Res
import kabanchik.features.common.chat.logic.generated.resources.chat_file_open_error
import ru.kabanchik.common.tools.textResource.TextResource

fun chatFileOpenErrorText(): TextResource = TextResource.Id(Res.string.chat_file_open_error)
