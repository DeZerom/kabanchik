package ru.kabanchik.common.network.internal.ws

internal fun createAuthHeader(token: String): Map<String, String> {
    return mapOf(
        "Authorization" to "Bearer $token"
    )
}