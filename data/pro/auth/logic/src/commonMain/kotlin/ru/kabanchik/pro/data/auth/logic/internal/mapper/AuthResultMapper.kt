package ru.kabanchik.pro.data.auth.logic.internal.mapper

import ru.kabanchik.pro.data.auth.model.ApiProAuthResult
import ru.kabanchik.pro.domain.auth.model.AuthResult

internal fun ApiProAuthResult.toDomain(): AuthResult = AuthResult(
    status = status,
    message = message
)