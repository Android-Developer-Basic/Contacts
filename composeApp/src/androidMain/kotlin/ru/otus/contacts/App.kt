package ru.otus.contacts

import android.app.Application
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Napier.base(DebugAntilog())
        Napier.i { "Android app start" }
    }
}