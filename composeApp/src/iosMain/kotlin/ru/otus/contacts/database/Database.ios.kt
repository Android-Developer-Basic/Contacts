package ru.otus.contacts.database

import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import co.touchlab.sqliter.DatabaseConfiguration
import io.github.aakira.napier.Napier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import ru.otus.contacts.utils.SuspendLazy

actual class ContactsDbProviderImpl : ContactsDbProvider {
    private val lazyDb = SuspendLazy {
        Napier.i { "Creating iOS database..." }
        withContext(Dispatchers.IO) {
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

    actual override suspend fun getDb(): ContactsDb = lazyDb()
}