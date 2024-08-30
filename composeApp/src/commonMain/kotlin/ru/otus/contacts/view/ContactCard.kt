package ru.otus.contacts.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import contacts.composeapp.generated.resources.Res
import contacts.composeapp.generated.resources.contacts
import contacts.composeapp.generated.resources.email_content
import contacts.composeapp.generated.resources.phone_content
import org.jetbrains.compose.resources.stringResource
import ru.otus.contacts.data.UiGesture
import ru.otus.contacts.data.UiState

@Composable
fun ContactCard(state: UiState.ContactCard, communication: Communication, onGesture: (UiGesture) -> Unit) {
    val contact by remember(state.contact) { derivedStateOf { state.contact } }

    ScreenScaffold(
        title = stringResource(Res.string.contacts),
        onGesture = onGesture,
        content = { p, g ->
            Column(
                modifier = Modifier.fillMaxSize().padding(p),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {

                AsyncImage(
                    modifier = Modifier
                        .size(240.dp)
                        .border(
                            BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface),
                            CircleShape
                        )
                        .clip(CircleShape),
                    model = contact.userPic,
                    contentDescription = contact.name
                )

                Text(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    text = contact.name,
                    style = MaterialTheme.typography.headlineLarge,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Card(Modifier.fillMaxWidth().padding(16.dp)) {
                    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                        contact.phone?.let {
                            DataRow(
                                Icons.Filled.Phone,
                                stringResource(Res.string.phone_content),
                                it
                            ) {
                                communication.callPhone(it)
                            }
                        }
                        contact.email?.let {
                            DataRow(
                                Icons.Filled.Email,
                                stringResource(Res.string.email_content),
                                it
                            ) {
                                communication.sendMail(it)
                            }
                        }
                    }
                }
            }
        }
    )
}

@Composable
private fun DataRow(icon: ImageVector, contentDescription: String, text: String, onClick: (String) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable { onClick(text) },
        horizontalArrangement = Arrangement.spacedBy(24.dp, Alignment.Start),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription
        )

        Text(
            text = text,
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Start
        )
    }
}