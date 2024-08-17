package ru.otus.contacts.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import contacts.composeapp.generated.resources.Res
import contacts.composeapp.generated.resources.btn_refresh_content
import contacts.composeapp.generated.resources.contacts
import contacts.composeapp.generated.resources.input_filter
import org.jetbrains.compose.resources.stringResource
import ru.otus.contacts.data.Contact
import ru.otus.contacts.data.UiGesture
import ru.otus.contacts.data.UiState

@Composable
@OptIn(ExperimentalFoundationApi::class)
fun ContactList(state: UiState.ContactList, onGesture: (UiGesture) -> Unit) {
    ScreenScaffold(
        title = stringResource(Res.string.contacts),
        onGesture = onGesture,
        actions = {
            IconButton(onClick = { onGesture(UiGesture.Contacts.Refresh) }) {
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
                            ContactView(it)
                        }
                    }
                }
            }
        }
    )
}

@Composable
private fun ContactView(data: Contact) {
    val bgColor = MaterialTheme.colorScheme.primary
    val textColor = MaterialTheme.colorScheme.onPrimary

    Row(
        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
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