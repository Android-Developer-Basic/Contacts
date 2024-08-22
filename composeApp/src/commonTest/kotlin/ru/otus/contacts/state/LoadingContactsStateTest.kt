package ru.otus.contacts.state

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.kodein.mock.Mock
import org.kodein.mock.UsesMocks
import ru.otus.contacts.data.ContactsDataState
import ru.otus.contacts.data.ErrorCode
import ru.otus.contacts.data.HttpException
import ru.otus.contacts.data.LoginFormData
import ru.otus.contacts.data.SessionClaims
import ru.otus.contacts.data.UiGesture
import ru.otus.contacts.data.UiState
import ru.otus.contacts.network.ContactsApi
import ru.otus.contacts.usecase.LoadContacts
import kotlin.coroutines.suspendCoroutine
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
@UsesMocks(
    ContactsApi::class,
    LoadContacts::class
)
internal class LoadingContactsStateTest : BaseStateTest() {
    @Mock
    lateinit var api: ContactsApi

    @Mock
    lateinit var loadContacts: LoadContacts

    private lateinit var state: LoadingContactsState

    private val sessionClaims = SessionClaims(U_NAME, TOKEN)

    override fun doInit() {
        injectMocks(mocker)
        state = LoadingContactsState(
            context,
            sessionClaims,
            dbProvider,
            loadContacts
        )
    }

    @Test
    fun switchesToContactListIfCacheIsThere() = runTest {
        everySuspending { db.hasLocalContacts(isNotNull()) } returns true
        every { factory.contactList(isNotNull()) } returns nextState

        state.start(stateMachine)

        verifyWithSuspend {
            stateMachine.setUiState(UiState.Loading(STRING_RESOURCE))
            db.hasLocalContacts(U_NAME)
            factory.contactList(ContactsDataState(sessionClaims))
            stateMachine.setMachineState(nextState)
        }
    }

    @Test
    fun loadsContactsIfCacheNotFound() = runTest {
        everySuspending { db.hasLocalContacts(isNotNull()) } returns false
        everySuspending { loadContacts.invoke(isNotNull(), isNotNull()) } returns Unit
        every { factory.contactList(isNotNull()) } returns nextState

        state.start(stateMachine)

        verifyWithSuspend {
            stateMachine.setUiState(UiState.Loading(STRING_RESOURCE))
            db.hasLocalContacts(U_NAME)
            loadContacts.invoke(sessionClaims)
            factory.contactList(ContactsDataState(sessionClaims))
            stateMachine.setMachineState(nextState)
        }
    }

    @Test
    fun switchesToErrorOnError() = runTest {
        everySuspending { db.hasLocalContacts(isNotNull()) } returns false
        everySuspending { loadContacts.invoke(isNotNull(), isNotNull()) } runs {
            throw HttpException(ErrorCode.NOT_FOUND, "User not found")
        }
        every { factory.loadingContactsError(isNotNull(), isNotNull(), isNotNull()) } returns nextState

        state.start(stateMachine)

        verifyWithSuspend(exhaustive = false) {
            factory.loadingContactsError(sessionClaims, ErrorCode.NOT_FOUND, "User not found")
            stateMachine.setMachineState(nextState)
        }

    }

    @Test
    fun returnsToFormOnBack() = runTest {
        everySuspending { db.hasLocalContacts(isNotNull()) } runs {
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