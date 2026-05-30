package ru.kabanchik.common.network.internal.ws

internal fun createStompHeaders(token: String): Map<String, String> {
    return mapOf(
        "Authorization" to "Bearer $token",
        "Heart-beat" to "5000,5000"
    )
}
