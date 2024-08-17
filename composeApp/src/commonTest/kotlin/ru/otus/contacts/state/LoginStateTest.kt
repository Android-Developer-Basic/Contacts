package ru.otus.contacts.state

import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.otus.contacts.data.LoginFormData
import ru.otus.contacts.data.UiGesture
import ru.otus.contacts.data.UiState
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class LoginStateTest : BaseStateTest() {

    private lateinit var state: LoginState

    override fun doInit() {
        state = LoginState(context, LoginFormData(U_NAME, U_PASS))
    }

    @Test
    fun updatesUiOnStart() {
        state.start(stateMachine)
        verify {
            stateMachine.setUiState(UiState.LoginForm(
                U_NAME,
                U_PASS,
                true
            ))
        }
    }

    @Test
    fun updatesUsername() {
        state.start(stateMachine)
        state.process(UiGesture.Login.UserName("login"))

        verify {
            stateMachine.setUiState(UiState.LoginForm(
                U_NAME,
                U_PASS,
                true
            ))
            stateMachine.setUiState(UiState.LoginForm(
                "login",
                U_PASS,
                true
            ))
        }
    }

    @Test
    fun disablesLoginIfUserIsEmpty() {
        state.start(stateMachine)
        state.process(UiGesture.Login.UserName(""))

        verify {
            stateMachine.setUiState(UiState.LoginForm(
                U_NAME,
                U_PASS,
                true
            ))
            stateMachine.setUiState(UiState.LoginForm(
                "",
                U_PASS,
                false
            ))
        }
    }

    @Test
    fun updatesPassword() {
        state.start(stateMachine)
        state.process(UiGesture.Login.Password("password"))

        verify {
            stateMachine.setUiState(UiState.LoginForm(
                U_NAME,
                U_PASS,
                true
            ))
            stateMachine.setUiState(UiState.LoginForm(
                U_NAME,
                "password",
                true
            ))
        }
    }

    @Test
    fun disablesLoginIfPasswordIsEmpty() {
        state.start(stateMachine)
        state.process(UiGesture.Login.Password(""))

        verify {
            stateMachine.setUiState(UiState.LoginForm(
                U_NAME,
                U_PASS,
                true
            ))
            stateMachine.setUiState(UiState.LoginForm(
                U_NAME,
                "",
                false
            ))
        }
    }

    @Test
    fun proceedsToLoginIfFilled() {
        every { factory.loggingIn(isNotNull()) } returns nextState

        state.start(stateMachine)
        state.process(UiGesture.Action)

        verify {
            stateMachine.setUiState(UiState.LoginForm(
                U_NAME,
                U_PASS,
                true
            ))
            factory.loggingIn(LoginFormData(U_NAME, U_PASS))
            stateMachine.setMachineState(nextState)
        }
    }

    @Test
    fun doesNotProceedToLoginWithEmptyName() {
        every { factory.loggingIn(isNotNull()) } returns nextState

        state.start(stateMachine)
        state.process(UiGesture.Login.UserName(""))
        state.process(UiGesture.Action)

        verify {
            stateMachine.setUiState(UiState.LoginForm(
                U_NAME,
                U_PASS,
                true
            ))
            stateMachine.setUiState(UiState.LoginForm(
                "",
                U_PASS,
                false
            ))
        }
    }

    @Test
    fun doesNotProceedToLoginWithEmptyPassword() {
        every { factory.loggingIn(isNotNull()) } returns nextState

        state.start(stateMachine)
        state.process(UiGesture.Login.Password(""))
        state.process(UiGesture.Action)

        verify {
            stateMachine.setUiState(UiState.LoginForm(
                U_NAME,
                U_PASS,
                true
            ))
            stateMachine.setUiState(UiState.LoginForm(
                U_NAME,
                "",
                false
            ))
        }
    }

    @Test
    fun terminatesOnBack() {
        every { factory.terminated() } returns nextState

        state.start(stateMachine)
        state.process(UiGesture.Back)

        verify {
            stateMachine.setUiState(UiState.LoginForm(
                U_NAME,
                U_PASS,
                true
            ))

            factory.terminated()
            stateMachine.setMachineState(nextState)
        }
    }
}