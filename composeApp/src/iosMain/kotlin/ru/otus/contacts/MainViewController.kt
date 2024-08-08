package ru.otus.contacts

import androidx.compose.ui.window.ComposeUIViewController
import ru.otus.contacts.data.UiState

fun MainViewController() = ComposeUIViewController {
    App(
        state = UiState.LoginForm("user", "password"),
        onComplete = {},
        onGesture = {}
    )
}