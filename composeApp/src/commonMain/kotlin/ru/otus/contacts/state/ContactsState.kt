package ru.otus.contacts.state

import com.motorro.commonstatemachine.coroutines.CoroutineState
import io.github.aakira.napier.Napier
import ru.otus.contacts.data.UiGesture
import ru.otus.contacts.data.UiState

typealias ContactsState = CoroutineState<UiGesture, UiState>

internal abstract class BaseContactsState(context: ContactsContext) : ContactsState(), ContactsContext by context {
    /**
     * A part of [process] template to process UI gesture
     */
    override fun doProcess(gesture: UiGesture) {
        Napier.w { "Unhandled gesture: $gesture" }
    }
}

