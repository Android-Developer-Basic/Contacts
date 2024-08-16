package ru.otus.contacts.database

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import io.github.aakira.napier.Napier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.otus.contacts.utils.SuspendLazy
import java.io.File
import java.util.Properties

actual class ContactsDbProviderImpl : ContactsDbProvider{
    private val lazyDb = SuspendLazy {
        Napier.i { "Creating Desktop database..." }
        withContext(Dispatchers.IO) {
            val parentFolder = File(System.getProperty("user.home") + "/Contacts")
            if (!parentFolder.exists()) {
                parentFolder.mkdirs()
            }
            val databasePath = File(parentFolder, "Contacts.db")
            val driver = JdbcSqliteDriver(
                url = "jdbc:sqlite:${databasePath.absolutePath}",
                properties = Properties().apply { put("foreign_keys", "true") }
            )
            if (databasePath.exists().not()) {
                ContactsDao.Schema.create(driver).await()
            }

            ContactsDbImpl(driver)
        }
    }

    actual override suspend fun getDb(): ContactsDb = lazyDb()
}