package ru.kabanchik.client.data.auth.logic.internal.mapper

import ru.kabanchik.client.data.auht.model.ApiAuthResult
import ru.kabanchik.client.domain.auth.model.AuthResult

internal fun ApiAuthResult.toDomain(): AuthResult = AuthResult(
    token = token
)