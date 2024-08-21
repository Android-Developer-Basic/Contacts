package ru.otus.contacts.state

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import ru.otus.contacts.data.ErrorCode
import ru.otus.contacts.data.SessionClaims
import ru.otus.contacts.data.UiGesture
import ru.otus.contacts.data.UiState
import kotlin.coroutines.suspendCoroutine
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class ContactCardStateTest : BaseStateTest() {
    private lateinit var state: ContactCardState

    private val sessionClaims = SessionClaims(U_NAME, TOKEN)
    private val filter = "filter"
    private val contactId = contact1.id

    override fun doInit() {
        state = ContactCardState(
            context,
            sessionClaims,
            filter,
            contactId,
            dbProvider
        )
    }

    @Test
    fun loadsContactOnStart() = runTest(dispatcher) {
        every { db.getContact(isNotNull(), isNotNull()) } returns flowOf(contact1)

        state.start(stateMachine)
        advanceUntilIdle()

        verifyWithSuspend {
            db.getContact(U_NAME, contactId)
            stateMachine.setUiState(UiState.ContactCard(contact1))
        }
    }

    @Test
    fun transfersToErrorIfNotFound() = runTest(dispatcher) {
        every { db.getContact(isNotNull(), isNotNull()) } returns flowOf(null)
        every { factory.contactCardError(
            isNotNull(),
            isNotNull(),
            isNotNull(),
            isNotNull(),
            isNotNull()
        ) } returns nextState

        state.start(stateMachine)
        advanceUntilIdle()

        verifyWithSuspend {
            db.getContact(U_NAME, contactId)
            factory.contactCardError(
                sessionClaims,
                filter,
                contactId,
                ErrorCode.NOT_FOUND,
                ErrorCode.NOT_FOUND.defaultMessage
            )
            stateMachine.setMachineState(nextState)
        }
    }

    @Test
    fun returnsToListOnBack() = runTest {
        every { db.getContact(isNotNull(), isNotNull()) } returns flow {
            suspendCoroutine {  }
        }
        every { factory.contactList(isNotNull(), isAny()) } returns nextState

        state.start(stateMachine)
        state.process(UiGesture.Back)

        verify(exhaustive = false) {

            stateMachine.setMachineState(nextState)
        }
    }

}