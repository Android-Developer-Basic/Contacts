package ru.otus.contacts.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import contacts.composeapp.generated.resources.Res
import contacts.composeapp.generated.resources.dismiss
import contacts.composeapp.generated.resources.error
import org.jetbrains.compose.resources.stringResource
import ru.otus.contacts.data.UiGesture
import ru.otus.contacts.data.UiState

@Composable
fun FatalError(
    errorMessage: String,
    onDismiss: () -> Unit,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(30.dp, Alignment.CenterVertically)
    ) {
        Icon(modifier = Modifier.size(128.dp), tint = MaterialTheme.colorScheme.error, imageVector = Icons.Filled.Warning, contentDescription = "Warning")
        Text(text = errorMessage, style = MaterialTheme.typography.bodyLarge)
        Button(onClick = onDismiss) {
            Text(text = stringResource(Res.string.dismiss))
        }
    }
}

@Composable
fun FatalErrorScreen(state: UiState.Error, onGesture: (UiGesture) -> Unit) {
    ScreenScaffold(
        title = state.title ?: stringResource(Res.string.error),
        onGesture = onGesture,
        content = { p, g ->  FatalError(state.message, { onGesture(UiGesture.Action) }, p) }
    )
}
