package ru.otus.contacts.state

import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.otus.contacts.data.Contact
import ru.otus.contacts.data.HttpException
import ru.otus.contacts.data.LoginFormData
import ru.otus.contacts.data.SessionClaims
import ru.otus.contacts.data.UiGesture
import ru.otus.contacts.data.UiState
import ru.otus.contacts.database.ContactsDbProvider
import ru.otus.contacts.usecase.LoadContacts

internal class ContactListState(
    context: ContactsContext,
    private val sessionClaims: SessionClaims,
    private val contactsDbProvider: ContactsDbProvider,
    private val doLoadContacts: LoadContacts,
    filterValue: String,
    private val transformDispatcher: CoroutineDispatcher = Dispatchers.Default
) : BaseContactsState(context) {

    private val filter = MutableStateFlow(filterValue)
    private var refreshing = MutableStateFlow(false)

    /**
     * A part of [start] template to initialize state
     */
    override fun doStart() {
        Napier.i { "Contact list..." }
        subscribeContacts()
    }

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    private fun subscribeContacts() {
        stateScope.launch {
            val db = contactsDbProvider.getDb()
            val loadContacts: (String) -> Flow<Map<Char, List<Contact>>> = {
                db.listContacts(sessionClaims.username, it.takeIf { it.isNotBlank() })
                    .map { contacts -> contacts.groupBy { contact -> contact.name.first() } }
                    .flowOn(transformDispatcher)
            }

            combine(
                filter,
                filter.debounce(DEBOUNCE).flatMapLatest(loadContacts),
                refreshing,
                transform = { f, c, r ->
                    UiState.ContactList(sessionClaims.username, f, c, r)
                }
            ).collect(::setUiState)
        }
    }

    private fun refresh() {
        if (refreshing.value) return
        stateScope.launch {
            refreshing.emit(true)
            Napier.i { "Refreshing contacts..." }
            try {
                doLoadContacts(sessionClaims)
                refreshing.emit(false)
            } catch (e: HttpException) {
                Napier.w(e) { "Error refreshing contacts" }
                setMachineState(factory.loadingContactsError(
                    sessionClaims,
                    e.code,
                    e.message ?: e.code.defaultMessage
                ))
            }
        }
    }

    /**
     * A part of [process] template to process UI gesture
     */
    override fun doProcess(gesture: UiGesture) {
        when (gesture) {
            is UiGesture.Contacts.Filter -> {
                filter.tryEmit(gesture.value)
            }
            UiGesture.Contacts.Refresh -> refresh()
            is UiGesture.Contacts.Click -> {
                Napier.i { "Selecting contact: ${gesture.contactId}" }
                setMachineState(factory.contactCard(
                    sessionClaims,
                    filter.value,
                    gesture.contactId
                ))
            }
            UiGesture.Back -> {
                Napier.i { "Back. Terminating..." }
                setMachineState(factory.login(LoginFormData(userName = sessionClaims.username)))
            }
            else -> super.doProcess(gesture)
        }
    }

    companion object {
        internal const val DEBOUNCE = 300L
    }
}