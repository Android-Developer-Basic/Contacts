package ru.otus.contacts

import androidx.lifecycle.ViewModel
import ru.otus.contacts.data.UiGesture

class MainActivityViewModel : ViewModel() {
    private val model = Model()

    val uiState get() = model.uiState
    fun onGesture(gesture: UiGesture) = model.onGesture(gesture)
}