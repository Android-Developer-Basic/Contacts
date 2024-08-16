package ru.otus.contacts

import com.motorro.commonstatemachine.coroutines.FlowStateMachine
import ru.otus.contacts.data.LoginFormData
import ru.otus.contacts.data.UiGesture
import ru.otus.contacts.data.UiState
import ru.otus.contacts.state.ContactsFactory

class Model(private val factory: ContactsFactory) {
    private val machine = FlowStateMachine(UiState.Loading("Loading")){ factory.login(LoginFormData()) }

    val uiState get() = machine.uiState
    fun onGesture(gesture: UiGesture) = machine.process(gesture)
}