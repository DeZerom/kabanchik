package ru.kabanchik.client.feature.auth.internal.register

import kabanchik.features.client.auth.generated.resources.Res
import kabanchik.features.client.auth.generated.resources.auth_reg_has_acc_error
import ru.kabanchik.common.errorHandler.logic.api.ErrorType
import ru.kabanchik.common.tools.textResource.TextResource

internal fun createErrorText(errorType: ErrorType): TextResource {
    return when (errorType) {
        ErrorType.BadRequest -> TextResource.Id(Res.string.auth_reg_has_acc_error)
        ErrorType.Unauthorized, ErrorType.Unknown -> errorType.defaultMessage
    }
}