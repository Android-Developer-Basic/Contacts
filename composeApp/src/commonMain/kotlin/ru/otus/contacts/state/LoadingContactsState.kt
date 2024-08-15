package ru.otus.contacts.state

import contacts.composeapp.generated.resources.Res
import contacts.composeapp.generated.resources.loading_contacts
import io.github.aakira.napier.Napier
import kotlinx.coroutines.launch
import ru.otus.contacts.data.SessionClaims
import ru.otus.contacts.data.UiGesture
import ru.otus.contacts.data.UiState

internal class LoadingContactsState(context: ContactsContext, sessionClaims: SessionClaims) : BaseContactsState(context) {
    /**
     * A part of [start] template to initialize state
     */
    override fun doStart() {
        Napier.i { "Loading contacts..." }
        loadContacts()
    }

    private fun loadContacts() {
        stateScope.launch {
            setUiState(UiState.Loading(resourceWrapper.getString(Res.string.loading_contacts)))
        }
    }

    /**
     * A part of [process] template to process UI gesture
     */
    override fun doProcess(gesture: UiGesture) = when(gesture) {
        UiGesture.Back -> {
            Napier.i { "Back. Terminating..." }
            setMachineState(factory.terminated())
        }
        else -> super.doProcess(gesture)
    }
}