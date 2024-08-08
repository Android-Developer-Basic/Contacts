package ru.otus.contacts.view

import androidx.compose.runtime.Composable
import androidx.activity.compose.BackHandler as AndroidBackHandler

@Composable
actual fun BackHandler(onBack: () -> Unit) {
    AndroidBackHandler(onBack = onBack)
}