package ru.otus.contacts

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import io.github.aakira.napier.Napier
import ru.otus.contacts.data.UiState

fun main() = application {
    Napier.d { "Desktop app start" }
    Window(
        onCloseRequest = ::exitApplication,
        title = "Contacts",
    ) {
        App(
            state = UiState.LoginForm("user", "password"),
            onComplete = { exitApplication() },
            onGesture = {}
        )
    }
}