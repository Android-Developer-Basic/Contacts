package ru.otus.contacts

import platform.UIKit.UIDevice

class IOSPlatform: Platform, CommonPlatform by CommonPlatform.Impl {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
    override val host: String = "127.0.0.1"
}

actual fun getPlatform(): Platform = IOSPlatform()