package ru.otus.contacts

import com.github.javafaker.Faker
import ru.otus.contacts.data.Contact

private const val DEFAULT_COUNT = 30
private const val MAX_COUNT = 100
private val faker = Faker()

fun getContacts(number: Int? = null): Array<Contact> = Array((number ?: DEFAULT_COUNT).coerceIn(0..MAX_COUNT)) { index ->
    Contact(
        id = "contact$index",
        name = faker.name().fullName(),
        email = faker.internet().emailAddress(),
        phone = faker.phoneNumber().phoneNumber(),
        userPic = faker.internet().avatar()
    )
}