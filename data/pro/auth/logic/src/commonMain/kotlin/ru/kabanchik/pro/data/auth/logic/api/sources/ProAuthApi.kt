package ru.kabanchik.pro.data.auth.logic.api.sources

import ru.kabanchik.pro.data.auth.model.ApiProAuthResult
import ru.kabanchik.pro.data.auth.model.ApiProCredentials

interface ProAuthApi {
    suspend fun authorize(credentials: ApiProCredentials): ApiProAuthResult
}