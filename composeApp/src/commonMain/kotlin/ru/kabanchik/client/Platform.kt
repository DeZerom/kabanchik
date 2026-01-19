package ru.kabanchik.client

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform