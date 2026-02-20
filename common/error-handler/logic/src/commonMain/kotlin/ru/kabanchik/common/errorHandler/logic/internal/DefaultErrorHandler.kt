package ru.kabanchik.common.errorHandler.logic.internal

import io.ktor.client.plugins.ResponseException
import ru.kabanchik.common.errorHandler.logic.api.ErrorHandler
import ru.kabanchik.common.errorHandler.logic.api.ErrorType

internal class DefaultErrorHandler : ErrorHandler {
    override fun handleError(error: Throwable): ErrorType {
        return if (error is ResponseException) {
            when (error.response.status.value) {
                400 -> ErrorType.BadRequest
                401 -> ErrorType.Unauthorized
                else -> ErrorType.Unknown
            }
        } else {
            ErrorType.Unknown
        }
    }
}