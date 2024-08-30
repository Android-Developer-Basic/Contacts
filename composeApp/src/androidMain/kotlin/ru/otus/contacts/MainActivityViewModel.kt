package ru.otus.contacts

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import ru.otus.contacts.data.UiGesture
import ru.otus.contacts.state.ContactsFactoryImpl

class MainActivityViewModel(application: Application) : AndroidViewModel(application) {

    private val model = Model(
        ContactsFactoryImpl(getApplication<ContactsApp>().dbProvider)
    )

    val uiState get() = model.uiState
    fun onGesture(gesture: UiGesture) = model.onGesture(gesture)
}