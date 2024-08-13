package ru.otus.contacts

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.window.ComposeUIViewController

val viewModel = Model()

fun MainViewController() = ComposeUIViewController {
    val viewState by viewModel.uiState.collectAsState()
    App(
        state = viewState,
        onComplete = {},
        onGesture = { viewModel.onGesture(it) }
    )
}