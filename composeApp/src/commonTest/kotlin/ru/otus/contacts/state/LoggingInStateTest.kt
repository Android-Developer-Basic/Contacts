package ru.otus.contacts.state

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.kodein.mock.Mock
import org.kodein.mock.UsesMocks
import ru.otus.contacts.data.ErrorCode
import ru.otus.contacts.data.HttpResponse
import ru.otus.contacts.data.LoginFormData
import ru.otus.contacts.data.LoginRequest
import ru.otus.contacts.data.SessionClaims
import ru.otus.contacts.data.UiGesture
import ru.otus.contacts.data.UiState
import ru.otus.contacts.network.ContactsApi
import kotlin.coroutines.suspendCoroutine
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
@UsesMocks(
    ContactsApi::class,
)
internal class LoggingInStateTest : BaseStateTest() {
    @Mock
    lateinit var api: ContactsApi

    private lateinit var state: LoggingInState

    private val loginData = LoginFormData(U_NAME, U_PASS)
    private val sessionClaims = SessionClaims(U_NAME, TOKEN)

    override fun doInit() {
        injectMocks(mocker)
        state = LoggingInState(context, loginData, api)
    }

    @Test
    fun logsInAndSwitchesToContactSync() = runTest {
        everySuspending { api.login(isNotNull()) } returns HttpResponse.Data(sessionClaims)
        every { factory.loadingContacts(isNotNull()) } returns nextState

        state.start(stateMachine)

        verifyWithSuspend {
            stateMachine.setUiState(UiState.Loading(STRING_RESOURCE))
            api.login(LoginRequest(U_NAME, U_PASS))
            factory.loadingContacts(sessionClaims)
            stateMachine.setMachineState(nextState)
        }
    }

    @Test
    fun switchesToErrorOnError() = runTest {
        everySuspending { api.login(isNotNull()) } returns HttpResponse.Error(ErrorCode.FORBIDDEN, "No way")
        every { factory.loginError(isNotNull(), isNotNull(), isNotNull()) } returns nextState

        state.start(stateMachine)

        verifyWithSuspend {
            stateMachine.setUiState(UiState.Loading(STRING_RESOURCE))
            api.login(LoginRequest(U_NAME, U_PASS))
            factory.loginError(loginData, ErrorCode.FORBIDDEN, "No way")
            stateMachine.setMachineState(nextState)
        }

    }

    @Test
    fun returnsToFormOnBack() = runTest {
        everySuspending { api.login(isNotNull()) } runs {
            suspendCoroutine {  }
        }
        every { factory.login(isNotNull()) } returns nextState

        state.start(stateMachine)
        state.process(UiGesture.Back)

        verify(exhaustive = false) {
            factory.login(loginData)
            stateMachine.setMachineState(nextState)
        }
    }
}