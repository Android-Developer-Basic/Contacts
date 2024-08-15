package ru.otus.contacts.state

import contacts.composeapp.generated.resources.Res
import contacts.composeapp.generated.resources.logging_in
import io.github.aakira.napier.Napier
import kotlinx.coroutines.launch
import ru.otus.contacts.data.ErrorCode
import ru.otus.contacts.data.HttpResponse
import ru.otus.contacts.data.LoginFormData
import ru.otus.contacts.data.LoginRequest
import ru.otus.contacts.data.UiGesture
import ru.otus.contacts.data.UiState
import ru.otus.contacts.network.ContactsApi

internal class LoggingInState(
    context: ContactsContext,
    private val loginData: LoginFormData,
    private val api: ContactsApi
) : BaseContactsState(context) {
    /**
     * A part of [start] template to initialize state
     */
    override fun doStart() {
        Napier.i { "Logging in ${loginData.userName}..." }
        login()
    }

    private fun login() = stateScope.launch {
        setUiState(UiState.Loading(resourceWrapper.getString(Res.string.logging_in)))
        try {
            when (val result = api.login(LoginRequest(loginData.userName, loginData.password))) {
                is HttpResponse.Data -> {
                    Napier.i { "Logged-in successfully" }
                    setMachineState(factory.loadingContacts(result.data))
                }
                is HttpResponse.Error -> {
                    Napier.w { "HTTP error ${result.code}: ${result.message}" }
                    toError(result.code, result.message)
                }
            }
        } catch (e: Throwable) {
            Napier.w(e) { "Api call error" }
            toError(ErrorCode.UNKNOWN, e.message ?: ErrorCode.UNKNOWN.defaultMessage)
        }
    }

    private fun toError(code: ErrorCode, message: String) {
        setMachineState(factory.loginError(loginData, code, message))
    }

    /**
     * A part of [process] template to process UI gesture
     */
    override fun doProcess(gesture: UiGesture) = when(gesture) {
        UiGesture.Back -> {
            Napier.i { "Back. Returning to login form..." }
            setMachineState(factory.login(loginData))
        }
        else -> super.doProcess(gesture)
    }
}