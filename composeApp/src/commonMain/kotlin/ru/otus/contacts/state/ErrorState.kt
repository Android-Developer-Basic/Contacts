package ru.otus.contacts.state

import io.github.aakira.napier.Napier
import ru.otus.contacts.data.ErrorCode
import ru.otus.contacts.data.UiGesture
import ru.otus.contacts.data.UiState

internal class ErrorState(
    context: ContactsContext,
    private val code: ErrorCode,
    private val message: String,
    private val onBack: ContactsFactory.() -> ContactsState,
    private val onAction: ContactsFactory.() -> ContactsState
) : BaseContactsState(context) {
    /**
     * A part of [start] template to initialize state
     */
    override fun doStart() {
        Napier.i { "Displaying error $code: $message" }
        setUiState(UiState.Error(message = message, title = code.name))
    }

    /**
     * A part of [process] template to process UI gesture
     */
    override fun doProcess(gesture: UiGesture) = when(gesture) {
        UiGesture.Back -> {
            Napier.i { "Back" }
            setMachineState(factory.onBack())
        }
        UiGesture.Action -> {
            Napier.i { "Action" }
            setMachineState(factory.onAction())
        }
        else -> super.doProcess(gesture)
    }
}