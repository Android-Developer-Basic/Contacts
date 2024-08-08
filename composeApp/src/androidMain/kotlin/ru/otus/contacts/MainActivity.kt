package ru.otus.contacts

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import ru.otus.contacts.data.UiState

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            App(
                state = UiState.LoginForm("user", "password"),
//                state = UiState.Loading("Loading..."),
//                state = UiState.Error("Some error", "Error"),
                onComplete = {},
                onGesture = {}
            )
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App(
        state = UiState.LoginForm("user", "password"),
        onComplete = {},
        onGesture = {}
    )
}