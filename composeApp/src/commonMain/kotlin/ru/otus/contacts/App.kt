package ru.otus.contacts

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import org.jetbrains.compose.ui.tooling.preview.Preview
import ru.otus.contacts.data.UiGesture
import ru.otus.contacts.data.UiState
import ru.otus.contacts.view.ContactCard
import ru.otus.contacts.view.ContactList
import ru.otus.contacts.view.FatalErrorScreen
import ru.otus.contacts.view.LoadingScreen
import ru.otus.contacts.view.LoginScreen

@Composable
@Preview
fun App(state: UiState, onComplete: () -> Unit, onGesture: (UiGesture) -> Unit) {
    MaterialTheme {
        when(state) {
            is UiState.Loading -> LoadingScreen(state, onGesture)
            is UiState.Error -> FatalErrorScreen(state, onGesture)
            is UiState.LoginForm -> LoginScreen(state, onGesture)
            is UiState.ContactList -> ContactList(state, onGesture)
            is UiState.ContactCard -> ContactCard(state, onGesture)
            UiState.Terminated -> LaunchedEffect(state) { onComplete() }
        }
    }
}