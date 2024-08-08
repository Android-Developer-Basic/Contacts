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

    companion object {
        const val U_NAME = "name"
        const val U_PASS = "pass"
    }
}