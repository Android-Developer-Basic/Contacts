package ru.otus.contacts

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.window.ComposeUIViewController
import platform.posix.exit

val viewModel = Model()

fun MainViewController() = ComposeUIViewController {
    val viewState by viewModel.uiState.collectAsState()
    App(
        state = viewState,
        onComplete = { exit(0) },
        onGesture = { viewModel.onGesture(it) }
    )
}