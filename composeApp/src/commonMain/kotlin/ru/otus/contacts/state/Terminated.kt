package ru.otus.contacts.state

import io.github.aakira.napier.Napier
import ru.otus.contacts.data.UiState

internal class Terminated : ContactsState() {
    override fun doStart() {
        Napier.i { "Terminated..." }
        setUiState(UiState.Terminated)
    }
}