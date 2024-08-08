package ru.otus.contacts

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import ru.otus.contacts.data.UiState


class MainActivity : ComponentActivity() {

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val state by viewModel.uiState.collectAsState()
            App(
                state = state,
                onComplete = { finish() },
                onGesture = { viewModel.onGesture(it) }
            )
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App(
        state = UiState.LoginForm("user", "password", true),
        onComplete = {},
        onGesture = {}
    )
}