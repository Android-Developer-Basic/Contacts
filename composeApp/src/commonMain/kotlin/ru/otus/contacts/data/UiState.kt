package ru.otus.contacts.data

import androidx.compose.runtime.Immutable

/**
 * Application view-states
 */
sealed class UiState {
    data class Loading(val message: String? = null) : UiState()
    data class Error(val message: String, val title: String? = null) : UiState()
    data class LoginForm(
        val userName: String,
        val password: String,
        val loginButtonEnabled: Boolean
    ) : UiState()
    @Immutable
    data class ContactList(
        val userName: String,
        val filter: String,
        val contacts: Map<Char, List<Contact>>,
        val refreshing: Boolean,
        val firstVisibleItemIndex: Int
    ) : UiState()
    @Immutable
    data class ContactCard(val contact: Contact) : UiState()
    data object Terminated : UiState()
}