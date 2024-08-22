package ru.otus.contacts.data

data class ContactsDataState(
    val credentials: SessionClaims,
    val filter: String = "",
    val firstVisibleItemIndex: Int = 0,
    val contacts: Map<Char, List<Contact>> = emptyMap()
)