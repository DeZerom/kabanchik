package ru.kabanchik.common.domain.chat.logic.api

private const val MessageMaxLength = 4096

fun splitAndTrimMessage(message: String): List<String> {
    return message.trim().chunked(MessageMaxLength).ifEmpty { listOf("") }
}
