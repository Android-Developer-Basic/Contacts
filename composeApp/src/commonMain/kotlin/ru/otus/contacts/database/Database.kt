package ru.otus.contacts.database

import app.cash.sqldelight.Query
import app.cash.sqldelight.async.coroutines.awaitAsList
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.db.SqlDriver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import ru.otus.contacts.data.Contact

interface ContactsDb {
    /**
     * Rewrites contact book
     * @param userName User name to set contact book for
     * @param contacts A list of contacts to save
     */
    suspend fun upsertContactBook(userName: String, contacts: List<Contact>)

    /**
     * Appends contacts to contact book
     * @param userName User name to append contact book for
     * @param contacts A list of contacts to save
     */
    suspend fun appendContactBook(userName: String, contacts: List<Contact>)

    /**
     * Checks if user has local data
     * @param userName User name to check
     */
    suspend fun hasLocalContacts(userName: String): Boolean

    /**
     * Lists contacts with optional filtering
     * @param userName Bound user name
     * @param nameFilter Optional name filter
     */
    fun listContacts(userName: String, nameFilter: String? = null): Flow<List<Contact>>

    /**
     * Displays contact
     * @param userName Bound user name
     * @param contactId Contact ID
     */
    fun getContact(userName: String, contactId: String): Flow<Contact?>
}

internal class ContactsDbImpl(driver: SqlDriver) : ContactsDb {
    private val db = ContactsDao(driver)

    override suspend fun upsertContactBook(userName: String, contacts: List<Contact>) {
        db.contactsQueries.transaction {
            db.contactsQueries.deleteUser(userName)
            db.contactsQueries.createUser(Users(userName))
            doUpsertContacts(userName, contacts)
        }
    }

    override suspend fun appendContactBook(userName: String, contacts: List<Contact>) {
        db.contactsQueries.transaction {
            doUpsertContacts(userName, contacts)
        }
    }

    override suspend fun hasLocalContacts(userName: String): Boolean {
        return db.contactsQueries.getUser(userName).awaitAsList().isNotEmpty()
    }

    override fun listContacts(userName: String, nameFilter: String?): Flow<List<Contact>> {
        val contactsQuery = if (null != nameFilter) {
            db.contactsQueries.findContacts(userName, nameFilter)
        } else {
            db.contactsQueries.listAllContacts(userName)
        }

        return contactsQuery.asFlow().mapContacts()
    }

    override fun getContact(userName: String, contactId: String): Flow<Contact?> {
        return db.contactsQueries.selectContact(userName, contactId).asFlow().mapContacts().map { it.firstOrNull() }
    }

    private suspend fun doUpsertContacts(userName: String, contacts: List<Contact>) {
        contacts.forEach {
            db.contactsQueries.upsertContact(
                userName,
                it.id,
                it.name,
                it.email,
                it.phone,
                it.userPic
            )
        }
    }

    private fun Flow<Query<Contacts>>.mapContacts(): Flow<List<Contact>> = map { query ->
        withContext(Dispatchers.Default) {
            query.awaitAsList().map {
                Contact(
                    id = it.contactId,
                    name = it.name,
                    email = it.email,
                    phone = it.phone,
                    userPic = it.userPic
                )
            }
        }
    }
}

interface ContactsDbProvider {
    suspend fun getDb(): ContactsDb
}

expect class ContactsDbProviderImpl : ContactsDbProvider{
    override suspend fun getDb(): ContactsDb
}