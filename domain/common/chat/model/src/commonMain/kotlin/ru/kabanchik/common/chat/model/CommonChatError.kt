package ru.kabanchik.common.chat.model

/**
 * Ошибка обработки STOMP-команды из `/user/queue/errors`.
 */
data class CommonChatError(
    val code: CommonChatErrorCode,
    val content: String,
    val sessionId: String? = null,
)

enum class CommonChatErrorCode {
    ExecutorOffShift,
    Forbidden,
    SessionNotAvailable,
    Other,
}
