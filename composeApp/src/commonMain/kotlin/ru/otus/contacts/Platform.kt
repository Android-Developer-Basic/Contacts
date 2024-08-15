package ru.otus.contacts

interface Platform : CommonPlatform {
    val name: String
    val host: String
}

interface CommonPlatform {
    val port: Int

    object Impl : CommonPlatform {
        override val port: Int = 8080
    }
}

expect fun getPlatform(): Platform