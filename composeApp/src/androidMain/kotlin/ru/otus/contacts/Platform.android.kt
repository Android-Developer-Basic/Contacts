package ru.otus.contacts

import android.os.Build

class AndroidPlatform : Platform, CommonPlatform by CommonPlatform.Impl {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
    override val host: String = "10.0.2.2"
}

actual fun getPlatform(): Platform = AndroidPlatform()