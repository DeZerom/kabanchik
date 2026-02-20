package ru.kabanchik.common.errorHandler.logic.api

interface ErrorHandler {
    fun handleError(error: Throwable): ErrorType
}