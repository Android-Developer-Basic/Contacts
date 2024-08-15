package ru.otus.contacts.database

import android.content.Context
import androidx.sqlite.db.SupportSQLiteDatabase
import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

actual class ContactsDbProviderImpl(private val context: Context) : ContactsDbProvider {
    actual override suspend fun getDb(): ContactsDb = withContext(Dispatchers.IO) {
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