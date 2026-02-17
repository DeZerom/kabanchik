package ru.kabanchik.pro.data.auth.logic.internal.mapper

import ru.kabanchik.pro.data.auth.model.ApiProAuthResult
import ru.kabanchik.pro.domain.auth.model.ProAuthResult

internal fun ApiProAuthResult.toDomain(): ProAuthResult = ProAuthResult(
    token = token
)