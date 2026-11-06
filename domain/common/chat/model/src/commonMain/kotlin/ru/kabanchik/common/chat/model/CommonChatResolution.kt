package ru.kabanchik.common.chat.model

/**
 * Итог выполнения заявки, который выбирает оператор при завершении.
 * Сервер его пока не принимает: все завершенные заявки считаются [Closed].
 */
enum class CommonChatResolution {
    Closed,
}
