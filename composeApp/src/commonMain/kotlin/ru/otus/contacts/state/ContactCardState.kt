package ru.otus.contacts.state

import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.otus.contacts.data.ErrorCode
import ru.otus.contacts.data.SessionClaims
import ru.otus.contacts.data.UiGesture
import ru.otus.contacts.data.UiState
import ru.otus.contacts.database.ContactsDbProvider

internal class  ContactCardState(
    context: ContactsContext,
    private val sessionClaims: SessionClaims,
    private val filterValue: String,
    private val contactId: String,
    private val contactsDbProvider: ContactsDbProvider
) : BaseContactsState(context) {

    override fun doStart() {
        subscribeContact()
    }

    private fun subscribeContact() {
        Napier.i { "Getting contact $contactId..." }
        stateScope.launch {
            contactsDbProvider.getDb()
                .getContact(sessionClaims.username, contactId)
                .map(::checkNotNull)
                .map(UiState::ContactCard)
                .catch { e ->
                    Napier.w(e) { "Error getting contact" }
                    val code: ErrorCode
                    val message: String
                    when (e) {
                        is IllegalStateException -> {
                            code = ErrorCode.NOT_FOUND
                            message = ErrorCode.NOT_FOUND.defaultMessage
                        }
                        else -> {
                            code = ErrorCode.UNKNOWN
                            message = e.message ?: ErrorCode.UNKNOWN.defaultMessage
                        }
                    }
                    setMachineState(factory.contactCardError(
                        sessionClaims,
                        filterValue,
                        contactId,
                        code,
                        message
                    ))
                }
                .collect(::setUiState)
        }
    }

    /**
     * A part of [process] template to process UI gesture
     */
    override fun doProcess(gesture: UiGesture) {
        when (gesture) {
            UiGesture.Back -> {
                Napier.i { "Back. Returning to list..." }
                setMachineState(factory.contactList(
                    sessionClaims,
                    filterValue
                ))
            }
            else -> super.doProcess(gesture)
        }
    }
}