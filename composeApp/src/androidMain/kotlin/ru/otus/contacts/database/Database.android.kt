package ru.otus.contacts.database

import android.content.Context
import androidx.sqlite.db.SupportSQLiteDatabase
import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import io.github.aakira.napier.Napier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.otus.contacts.utils.SuspendLazy

actual class ContactsDbProviderImpl(private val context: Context) : ContactsDbProvider {
    private val lazyDb = SuspendLazy {
        Napier.i { "Creating Android database..." }
        withContext(Dispatchers.IO) {
            ContactsDbImpl(
                AndroidSqliteDriver(
                    schema = ContactsDao.Schema.synchronous(),
                    context = context,
                    name = "Contacts.db",
                    callback = object : AndroidSqliteDriver.Callback(ContactsDao.Schema.synchronous()) {
                        override fun onOpen(db: SupportSQLiteDatabase) {
                            db.setForeignKeyConstraintsEnabled(true)
                        }
                    }
                )
            )
        }
    }
    
    actual override suspend fun getDb(): ContactsDb = lazyDb()
}