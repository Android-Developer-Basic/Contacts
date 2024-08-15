package ru.otus.contacts.state

import ru.otus.contacts.ResourceWrapper

interface ContactsContext {
    val factory: ContactsFactory
    val resourceWrapper: ResourceWrapper
}