package ru.otus.contacts.data

sealed class UiGesture {
    data object Back : UiGesture()
    data object Action : UiGesture()

    sealed class Login : UiGesture() {
        data class UserName(val value: String) : Login()
        data class Password(val value: String) : Login()
    }

    sealed class Contacts : UiGesture() {
        data object Refresh : Contacts()
        data class Filter(val value: String) : Contacts()
    }
}