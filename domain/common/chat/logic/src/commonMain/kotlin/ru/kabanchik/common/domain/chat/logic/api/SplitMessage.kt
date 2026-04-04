package ru.kabanchik.common.domain.chat.logic.api

private const val MessageMaxLength = 4096

fun splitAndTrimMessage(message: String): List<String> {
    val trimmedMessage = message.trim()
    if (trimmedMessage.length <= MessageMaxLength) {
        return listOf(trimmedMessage)
    }

    val messages = mutableListOf<String>()
    for (i in 0..(trimmedMessage.length / MessageMaxLength)) {
        val startIndex = i * MessageMaxLength
        val endIndex = (startIndex + MessageMaxLength).coerceAtMost(trimmedMessage.length)
        val part = trimmedMessage.substring(startIndex = startIndex, endIndex = endIndex)

        messages.add(part)
    }

    return messages
}