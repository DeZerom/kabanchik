package ru.kabanchik.pro.domain.chat.logic.api

/**
 * Сервер отклонил команду оператора, потому что тот вне смены.
 */
class ExecutorOffShiftException : IllegalStateException("Executor is off shift")
