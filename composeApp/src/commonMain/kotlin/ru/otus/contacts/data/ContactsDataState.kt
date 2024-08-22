package ru.otus.contacts.data

data class ContactsDataState(
    val credentials: SessionClaims,
    val filter: String = "",
    val contacts: Map<Char, List<Contact>> = emptyMap()
)