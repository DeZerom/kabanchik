package ru.kabanchik.common.tools.network

const val BackendHost = "185.102.139.25:8080"
const val BackendBaseUrl = "http://$BackendHost"

fun String.withBackendBaseUrl(): String {
    if (startsWith("http://", ignoreCase = true) || startsWith("https://", ignoreCase = true)) {
        return this
    }

    return "$BackendBaseUrl/${trimStart('/')}"
}
