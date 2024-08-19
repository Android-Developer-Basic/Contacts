package ru.otus.contacts.state

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.yield
import org.kodein.mock.Mock
import org.kodein.mock.UsesMocks
import ru.otus.contacts.data.Contact
import ru.otus.contacts.data.LoginFormData
import ru.otus.contacts.data.SessionClaims
import ru.otus.contacts.data.UiGesture
import ru.otus.contacts.data.UiState
import ru.otus.contacts.database.ContactsDb
import ru.otus.contacts.usecase.LoadContacts
import kotlin.coroutines.suspendCoroutine
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
@UsesMocks(
    ContactsDb::class,
    LoadContacts::class
)
internal class ContactListStateTest : BaseStateTest() {
    @Mock
    lateinit var loadContacts: LoadContacts

    private lateinit var state: ContactListState

    private val sessionClaims = SessionClaims(U_NAME, TOKEN)
    private val filter = "filter"

    override fun doInit() {
        injectMocks(mocker)
        state = ContactListState(
            context,
            sessionClaims,
            dbProvider,
            loadContacts,
            filter,
            dispatcher
        )
    }

    @Test
    fun loadsContactsOnStart() = runTest(dispatcher) {
        every { db.listContacts(isNotNull(), isAny()) } returns flowOf(CONTACTS)

        state.start(stateMachine)
        advanceUntilIdle()

        verifyWithSuspend {
            db.listContacts(U_NAME, filter)
            stateMachine.setUiState(UiState.ContactList(
                sessionClaims.username,
                filter,
                mapOf(
                    'V' to listOf(contact1),
                    'P' to listOf(contact2)
                ),
                false
            ))
        }
    }

    @Test
    fun updatesWhenDatabaseUpdates() = runTest(dispatcher) {
        val contacts = MutableSharedFlow<List<Contact>>()
        every { db.listContacts(isNotNull(), isAny()) } returns contacts

        state.start(stateMachine)
        advanceUntilIdle()
        contacts.emit(listOf(contact1))
        advanceUntilIdle()
        contacts.emit(listOf(contact2))
        advanceUntilIdle()

        verifyWithSuspend {
            db.listContacts(U_NAME, filter)
            stateMachine.setUiState(UiState.ContactList(
                sessionClaims.username,
                filter,
                mapOf(
                    'V' to listOf(contact1)
                ),
                false
            ))
            stateMachine.setUiState(UiState.ContactList(
                sessionClaims.username,
                filter,
                mapOf(
                    'P' to listOf(contact2)
                ),
                false
            ))
        }
    }

    @Test
    fun updatesWithFilterChanges() = runTest(dispatcher) {
        val filter2 = "filter2"
        val listMock = every { db.listContacts(isNotNull(), isAny()) }

        listMock returns flowOf(listOf(contact1))
        state.start(stateMachine)
        advanceUntilIdle()

        listMock returns flowOf(listOf(contact2))
        state.process(UiGesture.Contacts.Filter(filter2))
        advanceUntilIdle()

        verifyWithSuspend {
            db.listContacts(U_NAME, filter)
            stateMachine.setUiState(UiState.ContactList(
                sessionClaims.username,
                filter,
                mapOf(
                    'V' to listOf(contact1)
                ),
                false
            ))
            stateMachine.setUiState(UiState.ContactList(
                sessionClaims.username,
                filter2,
                mapOf(
                    'V' to listOf(contact1)
                ),
                false
            ))
            db.listContacts(U_NAME, filter2)
            stateMachine.setUiState(UiState.ContactList(
                sessionClaims.username,
                filter2,
                mapOf(
                    'P' to listOf(contact2)
                ),
                false
            ))
        }
    }

    @Test
    fun refreshesTheList() = runTest(dispatcher) {
        every { db.listContacts(isNotNull(), isAny()) } returns flowOf(CONTACTS)
        everySuspending { loadContacts(isNotNull(), isNotNull()) } runs {
            yield()
        }

        state.start(stateMachine)
        advanceUntilIdle()
        state.process(UiGesture.Contacts.Refresh)
        advanceUntilIdle()

        verifyWithSuspend {
            db.listContacts(U_NAME, filter)
            stateMachine.setUiState(UiState.ContactList(
                sessionClaims.username,
                filter,
                mapOf(
                    'V' to listOf(contact1),
                    'P' to listOf(contact2)
                ),
                false
            ))
            loadContacts(sessionClaims)
            stateMachine.setUiState(UiState.ContactList(
                sessionClaims.username,
                filter,
                mapOf(
                    'V' to listOf(contact1),
                    'P' to listOf(contact2)
                ),
                true
            ))
            stateMachine.setUiState(UiState.ContactList(
                sessionClaims.username,
                filter,
                mapOf(
                    'V' to listOf(contact1),
                    'P' to listOf(contact2)
                ),
                false
            ))
        }
    }

    @Test
    fun returnsToFormOnBack() = runTest {
        every { db.listContacts(isNotNull(), isAny()) } returns flow {
            suspendCoroutine {  }
        }
        every { factory.login(isNotNull()) } returns nextState

        state.start(stateMachine)
        state.process(UiGesture.Back)

        verify(exhaustive = false) {
            factory.login(LoginFormData(U_NAME))
            stateMachine.setMachineState(nextState)
        }
    }
}