package ru.kabanchik.common.errorHandler.logic.api

import kabanchik.common.error_handler.logic.generated.resources.Res
import kabanchik.common.error_handler.logic.generated.resources.error_handler_bad_request
import kabanchik.common.error_handler.logic.generated.resources.error_handler_unauthorized
import kabanchik.common.error_handler.logic.generated.resources.error_handler_unknown
import ru.kabanchik.common.tools.textResource.TextResource

enum class ErrorType(val defaultMessage: TextResource) {
    BadRequest(TextResource.Id(Res.string.error_handler_bad_request)),
    Unauthorized(TextResource.Id(Res.string.error_handler_unauthorized)),
    Unknown(TextResource.Id(Res.string.error_handler_unknown))
}