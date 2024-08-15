package ru.otus.contacts

class JVMPlatform: Platform, CommonPlatform by CommonPlatform.Impl {
    override val name: String = "Java ${System.getProperty("java.version")}"
    override val host: String = "127.0.0.1"
}

actual fun getPlatform(): Platform = JVMPlatform()