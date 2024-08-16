package ru.otus.contacts

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.window.ComposeUIViewController
import platform.posix.exit
import ru.otus.contacts.database.ContactsDbProviderImpl
import ru.otus.contacts.state.ContactsFactoryImpl

val dbProvider = ContactsDbProviderImpl()
val factory = ContactsFactoryImpl(dbProvider)
val viewModel = Model(factory)

fun MainViewController() = ComposeUIViewController {
    val viewState by viewModel.uiState.collectAsState()
    App(
        state = viewState,
        onComplete = { exit(0) },
        onGesture = { viewModel.onGesture(it) }
    )
}