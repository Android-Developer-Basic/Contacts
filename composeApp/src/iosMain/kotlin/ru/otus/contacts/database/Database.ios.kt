package ru.otus.contacts.database

import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import co.touchlab.sqliter.DatabaseConfiguration
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

actual class ContactsDbProviderImpl : ContactsDbProvider {
    actual override suspend fun getDb(): ContactsDb = withContext(Dispatchers.IO) {
        ContactsDbImpl(
            NativeSqliteDriver(
                schema = ContactsDao.Schema.synchronous(),
                name = "Contacts.db",
                onConfiguration = { config: DatabaseConfiguration ->
                    config.copy(
                        extendedConfig = DatabaseConfiguration.Extended(foreignKeyConstraints = true)
                    )
                }
            )
        )
    }
}