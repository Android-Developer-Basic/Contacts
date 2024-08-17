package ru.otus.contacts.usecase

import io.github.aakira.napier.Napier
import ru.otus.contacts.data.ErrorCode
import ru.otus.contacts.data.HttpException
import ru.otus.contacts.data.HttpResponse
import ru.otus.contacts.data.SessionClaims
import ru.otus.contacts.database.ContactsDbProvider
import ru.otus.contacts.network.ContactsApi

/**
 * Updates contacts database for passed user
 */
interface LoadContacts {
    /**
     * Updates contacts database
     * @param claims Session claims
     */
    suspend operator fun invoke(claims: SessionClaims, pageSize: Int = 100)
}

internal class LoadContactsImpl(
    private val dbProvider: ContactsDbProvider,
    private val api: ContactsApi
) : LoadContacts {
    override suspend fun invoke(claims: SessionClaims, pageSize: Int) {
        try {
            Napier.i { "Loading contacts..." }
            when(val response = api.loadContacts(claims, pageSize)) {
                is HttpResponse.Data -> {
                    Napier.i { "Updating local database with ${response.data.contacts.size} contacts..." }
                    dbProvider.getDb().upsertContactBook(claims.username, response.data.contacts)
                }
                is HttpResponse.Error -> {
                    Napier.w { "Error loading contacts (${response.code}): ${response.message}" }
                    throw HttpException(response.code, response.message)
                }
            }

        } catch (e: Throwable) {
            Napier.w(e) { "Api call error" }
            throw(HttpException(ErrorCode.UNKNOWN, e.message ?: ErrorCode.UNKNOWN.defaultMessage))
        }
    }
}