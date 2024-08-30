package ru.otus.contacts

import android.app.Application
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import ru.otus.contacts.database.ContactsDbProvider
import ru.otus.contacts.database.ContactsDbProviderImpl

class ContactsApp : Application() {

    val dbProvider: ContactsDbProvider by lazy {
        ContactsDbProviderImpl(this)
    }

    override fun onCreate() {
        super.onCreate()
        Napier.base(DebugAntilog())
        Napier.i { "Android app start" }
    }
}