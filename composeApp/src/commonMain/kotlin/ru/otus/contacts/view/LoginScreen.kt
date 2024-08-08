package ru.otus.contacts.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import contacts.composeapp.generated.resources.Res
import contacts.composeapp.generated.resources.input_login
import contacts.composeapp.generated.resources.input_password
import contacts.composeapp.generated.resources.login
import org.jetbrains.compose.resources.stringResource
import ru.otus.contacts.data.UiGesture
import ru.otus.contacts.data.UiState

@Composable
fun LoginScreen(state: UiState.LoginForm, onGesture: (UiGesture) -> Unit) {
    ScreenScaffold(
        title = stringResource(Res.string.login),
        onGesture = onGesture,
        content = { p, g ->
            Column(
                modifier = Modifier.fillMaxSize().padding(p),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    modifier = Modifier.padding(16.dp),
                    text = stringResource(Res.string.login),
                    style = MaterialTheme.typography.titleLarge
                )
                TextField(
                    modifier = Modifier.padding(16.dp),
                    value = state.userName,
                    onValueChange = { onGesture(UiGesture.Login.UserName(it)) },
                    label = { Text(stringResource(Res.string.input_login)) }
                )
                TextField(
                    modifier = Modifier.padding(16.dp),
                    value = state.password,
                    onValueChange = { onGesture(UiGesture.Login.Password(it)) },
                    label = { Text(stringResource(Res.string.input_password)) },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                )
                Button(
                    modifier = Modifier.padding(16.dp),
                    onClick = { onGesture(UiGesture.Action) },
                    enabled = state.loginButtonEnabled
                ) {
                    Text(stringResource(Res.string.login))
                }
            }
        }
    )
}