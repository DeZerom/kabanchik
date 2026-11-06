package ru.kabanchik.common.data.chatDetails.model

/**
 * Известные значения [CommonApiErrorMessage.code] из `/user/queue/errors`.
 */
object CommonApiErrorCode {
    const val ExecutorOffShift: String = "EXECUTOR_OFF_SHIFT"
    const val Forbidden: String = "FORBIDDEN"
    const val SessionNotAvailable: String = "SESSION_NOT_AVAILABLE"
}
