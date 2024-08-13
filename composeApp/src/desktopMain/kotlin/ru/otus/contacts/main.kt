package ru.otus.contacts

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import io.github.aakira.napier.Napier

fun main() = application {
    Napier.d { "Desktop app start" }

    val viewModel = Model()

    Window(
        onCloseRequest = ::exitApplication,
        title = "Contacts",
    ) {
        val viewState by viewModel.uiState.collectAsState()
        App(
            state = viewState,
            onComplete = { exitApplication() },
            onGesture = { viewModel.onGesture(it) }
        )
    }
}