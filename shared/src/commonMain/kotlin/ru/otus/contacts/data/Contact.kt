package ru.otus.contacts.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("Contact")
data class Contact(
    val id: String,
    val name: String,
    val email: String? = null,
    val phone: String? = null,
    val userPic: String? = null
)

@Serializable
@SerialName("ContactsData")
data class ContactsData(val contacts: List<Contact>)