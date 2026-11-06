package ru.kabanchik.common.network.api

/**
 * Управление жизненным циклом STOMP-соединения со стороны приложения.
 */
interface StompConnectionController {
    /**
     * Приложение вернулось в foreground: если соединение потеряно,
     * переподключиться сразу, не дожидаясь очередной паузы backoff.
     */
    fun onAppForeground()
}
