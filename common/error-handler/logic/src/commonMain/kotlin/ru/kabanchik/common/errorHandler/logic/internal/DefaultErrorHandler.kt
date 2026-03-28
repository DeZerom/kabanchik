package ru.kabanchik.common.errorHandler.logic.internal

import dev.shivathapaa.logger.api.loggerE
import io.ktor.client.plugins.ResponseException
import ru.kabanchik.common.errorHandler.logic.api.ErrorHandler
import ru.kabanchik.common.errorHandler.logic.api.ErrorType

internal class DefaultErrorHandler : ErrorHandler {
    override fun handleError(error: Throwable): ErrorType {
        val errorType = if (error is ResponseException) {
            when (error.response.status.value) {
                400 -> ErrorType.BadRequest
                401 -> ErrorType.Unauthorized
                else -> ErrorType.Unknown
            }
        } else {
            ErrorType.Unknown
        }
        loggerE("Recognized error type: $errorType", error)

        return errorType
    }
}