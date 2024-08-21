package ru.otus.contacts.view

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import contacts.composeapp.generated.resources.Res
import contacts.composeapp.generated.resources.btn_refresh_content
import contacts.composeapp.generated.resources.contacts
import contacts.composeapp.generated.resources.input_filter
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import ru.otus.contacts.data.Contact
import ru.otus.contacts.data.UiGesture
import ru.otus.contacts.data.UiState

@Composable
@OptIn(ExperimentalFoundationApi::class)
fun ContactList(state: UiState.ContactList, onGesture: (UiGesture) -> Unit) {
    val angle = remember(state.refreshing) {
        Animatable(0f)
    }
    LaunchedEffect(state.refreshing) {
        launch {
            if (state.refreshing) {
                angle.animateTo(
                    targetValue = 360f,
                    animationSpec = infiniteRepeatable(
                        tween(2000, easing = LinearEasing)
                    )
                )
            }
        }
    }

    ScreenScaffold(
        title = stringResource(Res.string.contacts),
        onGesture = onGesture,
        actions = {
            IconButton(
                modifier = Modifier.rotate(angle.value),
                onClick = { onGesture(UiGesture.Contacts.Refresh) }
            ) {
                Icon(
                    imageVector = Icons.Filled.Refresh,
                    contentDescription = stringResource(Res.string.btn_refresh_content)
                )
            }
        },
        content = { p, g ->
            Column(
                modifier = Modifier.fillMaxSize().padding(p),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                TextField(
                    modifier = Modifier.fillMaxWidth().padding(10.dp),
                    value = state.filter,
                    onValueChange = { onGesture(UiGesture.Contacts.Filter(it)) },
                    label = { Text(stringResource(Res.string.input_filter)) },
                    trailingIcon = {
                        IconButton(onClick = { onGesture(UiGesture.Contacts.Filter("")) }) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = stringResource(Res.string.btn_refresh_content)
                            )
                        }
                    }
                )

                LazyColumn(Modifier.fillMaxSize()) {
                    state.contacts.forEach { (letter, contacts) ->
                        stickyHeader {
                            Box(Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.secondaryContainer)
                            ) {
                                Text(
                                    modifier = Modifier.padding(8.dp),
                                    text = letter.toString(),
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                            }
                        }
                        items(contacts, { it.id }) {
                            ContactView(it, onGesture)
                        }
                    }
                }
            }
        }
    )
}

@Composable
private fun ContactView(data: Contact, onGesture: (UiGesture) -> Unit) {
    Row(
        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp).clickable {
            onGesture(UiGesture.Contacts.Click(data.id))
        },
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            modifier = Modifier
                .size(48.dp)
                .border(
                    BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface),
                    CircleShape
                )
                .clip(CircleShape),
            model = data.userPic,
            contentDescription = data.name
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = data.name,
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Start,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}