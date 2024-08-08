package ru.otus.contacts.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import contacts.composeapp.generated.resources.Res
import contacts.composeapp.generated.resources.loading
import org.jetbrains.compose.resources.stringResource
import ru.otus.contacts.data.UiGesture
import ru.otus.contacts.data.UiState

@Composable
fun Loading(message: String?, contentPadding: PaddingValues = PaddingValues(0.dp)) {
    Column(
        modifier = Modifier.fillMaxSize().padding(contentPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator()
        message?.let {
            Text(
                text = it,
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun LoadingScreen(state: UiState.Loading, onGesture: (UiGesture) -> Unit) {
    ScreenScaffold(
        title = state.message ?: stringResource(Res.string.loading),
        onGesture = onGesture,
        content = { p, g ->  Loading(state.message, p) }
    )
}