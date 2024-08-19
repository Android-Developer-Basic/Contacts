package ru.otus.contacts.state

import ru.otus.contacts.data.Contact

const val U_NAME = "name"
const val U_PASS = "pass"
const val TOKEN = "token"

val contact1 = Contact(
    id = "contact1",
    name = "Vasya",
    email = "vasya@otus.ru",
    phone = "+7(916)1234567",
    userPic = "http://otus.ru/userpics/contact1.jpg"
)

val contact2 = Contact(
    id = "contact2",
    name = "Petya",
    email = "petya@otus.ru",
    phone = "+7(916)7654321",
    userPic = "http://otus.ru/userpics/contact2.jpg"
)

val CONTACTS = listOf(contact1, contact2)