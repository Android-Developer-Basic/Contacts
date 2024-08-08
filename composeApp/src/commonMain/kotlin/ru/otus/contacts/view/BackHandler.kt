package ru.otus.contacts.view

import androidx.compose.runtime.Composable

@Composable
expect fun BackHandler(onBack: () -> Unit)