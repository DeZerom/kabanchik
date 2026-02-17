package ru.kabanchik.common.domain.chatDetails.logic.api

private const val MessageMaxLength = 4096

fun splitMessage(message: String): List<String> {
    if (message.length <= MessageMaxLength) {
        return listOf(message)
    }

    val messages = mutableListOf<String>()
    for (i in 0..(message.length / MessageMaxLength)) {
        val startIndex = i * MessageMaxLength
        val endIndex = (startIndex + MessageMaxLength).coerceAtMost(message.length)
        val part = message.substring(startIndex = startIndex, endIndex = endIndex)

        messages.add(part)
    }

    return messages
}