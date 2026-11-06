package ru.kabanchik.common.chat.model

import kotlinx.datetime.LocalDate

sealed interface CommonChatMessage {
    data class Date(val date: LocalDate) : CommonChatMessage
    data class Message(val message: CommonMessage) : CommonChatMessage

    /**
     * Системное сообщение о завершении заявки.
     */
    data class SessionEnd(val message: CommonMessage) : CommonChatMessage
    data object OperatorFound : CommonChatMessage
}