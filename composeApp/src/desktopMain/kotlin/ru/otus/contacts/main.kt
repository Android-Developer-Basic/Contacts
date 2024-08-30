package ru.otus.contacts

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import io.github.aakira.napier.Napier
import ru.otus.contacts.database.ContactsDbProviderImpl
import ru.otus.contacts.state.ContactsFactoryImpl
import ru.otus.contacts.view.DesktopCommunication

fun main() = application {
    Napier.d { "Desktop app start" }

    val dbProvider = ContactsDbProviderImpl()
    val factory = ContactsFactoryImpl(dbProvider)
    val viewModel = Model(factory)

    Window(
        onCloseRequest = ::exitApplication,
        title = "Contacts",
    ) {
        val viewState by viewModel.uiState.collectAsState()
        App(
            state = viewState,
            communication = DesktopCommunication,
            onComplete = { exitApplication() },
            onGesture = { viewModel.onGesture(it) }
        )
    }
}