package ru.otus.contacts.view

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.text.style.TextOverflow
import ru.otus.contacts.data.UiGesture

@Composable
private fun BackButton(onGesture: (UiGesture) -> Unit) {
    IconButton(onClick = { onGesture(UiGesture.Back) }) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back"
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenScaffold(
    title: String,
    onGesture: (UiGesture) -> Unit,
    content: @Composable (PaddingValues, (UiGesture) -> Unit) -> Unit,
    navigationButton: @Composable () -> Unit = { BackButton(onGesture) },
    actions: @Composable RowScope.() -> Unit = {}
) {
    val snackbarHostState = remember { SnackbarHostState() }
    BackHandler { onGesture(UiGesture.Back) }
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = title,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = navigationButton,
                actions = actions
            )
        },
        content = { contentPadding ->
            content(contentPadding, onGesture)
        }
    )
}