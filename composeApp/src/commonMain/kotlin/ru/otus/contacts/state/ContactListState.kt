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
import ru.otus.contacts.data.ContactsDataState
import ru.otus.contacts.data.HttpException
import ru.otus.contacts.data.LoginFormData
import ru.otus.contacts.data.UiGesture
import ru.otus.contacts.data.UiState
import ru.otus.contacts.database.ContactsDbProvider
import ru.otus.contacts.usecase.LoadContacts
import kotlin.properties.Delegates

internal class ContactListState(
    context: ContactsContext,
    dataState: ContactsDataState,
    private val contactsDbProvider: ContactsDbProvider,
    private val doLoadContacts: LoadContacts,
    private val transformDispatcher: CoroutineDispatcher = Dispatchers.Default
) : BaseContactsState(context) {

    private val filter = MutableStateFlow(dataState.filter)
    private var refreshing = false
    private var dataState by Delegates.observable(dataState) { _, _, update ->
        render()
    }
    private var scroll = dataState.firstVisibleItemIndex

    /**
     * A part of [start] template to initialize state
     */
    override fun doStart() {
        Napier.i { "Contact list..." }
        render()
        subscribeContacts()
    }

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    private fun subscribeContacts() {
        stateScope.launch {
            val db = contactsDbProvider.getDb()
            val loadContacts: (String) -> Flow<Map<Char, List<Contact>>> = {
                db.listContacts(dataState.credentials.username, it.takeIf { it.isNotBlank() })
                    .map { contacts -> contacts.groupBy { contact -> contact.name.first() } }
                    .flowOn(transformDispatcher)
            }

            combine(
                filter,
                filter.debounce(DEBOUNCE).flatMapLatest(loadContacts),
                transform = ::Pair
            ).collect { (filter, contacts) ->
                dataState = dataState.copy(
                    filter = filter,
                    contacts = contacts,
                    firstVisibleItemIndex = 0
                )
            }
        }
    }

    private fun render()  {
        setUiState(UiState.ContactList(
            dataState.credentials.username,
            dataState.filter,
            dataState.contacts,
            refreshing,
            dataState.firstVisibleItemIndex
        ))
    }

    private fun refresh() {
        if (refreshing) return
        stateScope.launch {
            Napier.i { "Refreshing contacts..." }
            refreshing = true
            render()
            try {
                doLoadContacts(dataState.credentials)
                refreshing = false
                render()
            } catch (e: HttpException) {
                Napier.w(e) { "Error refreshing contacts" }
                setMachineState(factory.loadingContactsError(
                    dataState.credentials,
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
                setMachineState(factory.contactCard(dataState, gesture.contactId))
            }
            is UiGesture.Contacts.Scroll -> {
                dataState = dataState.copy(firstVisibleItemIndex = gesture.position)
            }
            UiGesture.Back -> {
                Napier.i { "Back. Terminating..." }
                setMachineState(factory.login(LoginFormData(userName = dataState.credentials.username)))
            }
            else -> super.doProcess(gesture)
        }
    }

    companion object {
        internal const val DEBOUNCE = 300L
    }
}