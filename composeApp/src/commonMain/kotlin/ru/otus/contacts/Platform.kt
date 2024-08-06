package ru.otus.contacts

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform