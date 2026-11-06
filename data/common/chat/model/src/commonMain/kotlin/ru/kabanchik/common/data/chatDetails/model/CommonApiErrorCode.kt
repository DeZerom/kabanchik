package ru.kabanchik.common.data.chatDetails.model

/**
 * Известные значения [CommonApiErrorMessage.code] из `/user/queue/errors`.
 */
object CommonApiErrorCode {
    const val ExecutorOffShift: String = "EXECUTOR_OFF_SHIFT"
    const val Forbidden: String = "FORBIDDEN"
    const val SessionNotAvailable: String = "SESSION_NOT_AVAILABLE"
    const val InvalidEndRequest: String = "INVALID_END_REQUEST"
    const val NotSessionExecutor: String = "NOT_SESSION_EXECUTOR"
    const val SessionNotOpen: String = "SESSION_NOT_OPEN"
    const val SessionEndFailed: String = "SESSION_END_FAILED"
    const val SessionClosed: String = "SESSION_CLOSED"
}
