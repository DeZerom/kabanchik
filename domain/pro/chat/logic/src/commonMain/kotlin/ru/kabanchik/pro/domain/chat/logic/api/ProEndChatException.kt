package ru.kabanchik.pro.domain.chat.logic.api

import ru.kabanchik.common.chat.model.CommonChatErrorCode

/**
 * Сервер не подтвердил завершение заявки.
 * [code] - код ошибки сервера, `null` - подтверждение не пришло.
 */
class ProEndChatException(val code: CommonChatErrorCode?) : IllegalStateException("Chat was not ended: $code")
