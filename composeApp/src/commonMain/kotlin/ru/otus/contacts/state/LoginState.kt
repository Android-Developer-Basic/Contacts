package ru.otus.contacts.state

import io.github.aakira.napier.Napier
import ru.otus.contacts.data.LoginFormData
import ru.otus.contacts.data.UiGesture
import ru.otus.contacts.data.UiState
import kotlin.properties.Delegates

internal class LoginState(
    context: ContactsContext,
    loginFormData: LoginFormData
) : BaseContactsState(context) {

    private var loginFormData: LoginFormData by Delegates.observable(loginFormData) { _, _, _ ->
        render()
    }

    /**
     * A part of [start] template to initialize state
     */
    override fun doStart() {
        Napier.i { "State started: ${this::class.simpleName}" }
        render()
    }

    /**
     * A part of [process] template to process UI gesture
     */
    override fun doProcess(gesture: UiGesture) = when(gesture) {
        is UiGesture.Login.UserName -> {
            loginFormData = loginFormData.copy(userName = gesture.value)
        }
        is UiGesture.Login.Password -> {
            loginFormData = loginFormData.copy(password = gesture.value)
        }
        UiGesture.Action -> TODO()
        UiGesture.Back -> TODO()
        else -> super.doProcess(gesture)
    }

    private fun render() {
        setUiState(UiState.LoginForm(
            userName = loginFormData.userName,
            password = loginFormData.password,
        ))
    }
}