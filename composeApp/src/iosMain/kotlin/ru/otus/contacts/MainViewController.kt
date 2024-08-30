package ru.otus.contacts

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.window.ComposeUIViewController
import platform.posix.exit
import ru.otus.contacts.database.ContactsDbProviderImpl
import ru.otus.contacts.state.ContactsFactoryImpl
import ru.otus.contacts.view.Communication

val dbProvider = ContactsDbProviderImpl()
val factory = ContactsFactoryImpl(dbProvider)
val viewModel = Model(factory)

fun MainViewController(communication: Communication) = ComposeUIViewController {
    val viewState by viewModel.uiState.collectAsState()
    App(
        state = viewState,
        communication = communication,
        onComplete = { exit(0) },
        onGesture = { viewModel.onGesture(it) }
    )
}