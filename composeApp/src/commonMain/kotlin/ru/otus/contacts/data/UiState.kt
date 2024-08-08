package ru.otus.contacts.data

/**
 * Application view-states
 */
sealed class UiState {
    data class Loading(val message: String? = null) : UiState()
    data class Error(val message: String, val title: String? = null) : UiState()
    data class LoginForm(val userName: String, val password: String): UiState()
    data object Terminated : UiState()
}