package ru.otus.contacts.state

import contacts.composeapp.generated.resources.Res
import contacts.composeapp.generated.resources.loading_contacts
import io.github.aakira.napier.Napier
import kotlinx.coroutines.launch
import ru.otus.contacts.data.ContactsDataState
import ru.otus.contacts.data.HttpException
import ru.otus.contacts.data.LoginFormData
import ru.otus.contacts.data.SessionClaims
import ru.otus.contacts.data.UiGesture
import ru.otus.contacts.data.UiState
import ru.otus.contacts.database.ContactsDbProvider
import ru.otus.contacts.usecase.LoadContacts

internal class LoadingContactsState(
    context: ContactsContext,
    private val sessionClaims: SessionClaims,
    private val contactsDbProvider: ContactsDbProvider,
    private val doLoadContacts: LoadContacts
) : BaseContactsState(context) {
    /**
     * A part of [start] template to initialize state
     */
    override fun doStart() {
        Napier.i { "Loading contacts..." }
        loadContacts()
    }

    private fun loadContacts() {
        stateScope.launch {
            setUiState(UiState.Loading(resourceWrapper.getString(Res.string.loading_contacts)))
            try {
                val db = contactsDbProvider.getDb()
                if (db.hasLocalContacts(sessionClaims.username).not()) {
                    Napier.i { "No local contacts database. Loading..." }
                    doLoadContacts(sessionClaims)
                }
                setMachineState(factory.contactList(ContactsDataState(sessionClaims)))
            } catch (e: HttpException) {
                Napier.w(e) { "Error updating contacts" }
                setMachineState(factory.loadingContactsError(sessionClaims, e.code, e.message ?: e.code.defaultMessage))
            }
        }
    }

    /**
     * A part of [process] template to process UI gesture
     */
    override fun doProcess(gesture: UiGesture) = when(gesture) {
        UiGesture.Back -> {
            Napier.i { "Back. Terminating..." }
            setMachineState(factory.login(LoginFormData(userName = sessionClaims.username)))
        }
        else -> super.doProcess(gesture)
    }
}