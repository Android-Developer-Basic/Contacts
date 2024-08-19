package ru.otus.contacts

import com.github.javafaker.Faker
import io.ktor.http.Parameters
import io.ktor.http.URLBuilder
import io.ktor.http.URLProtocol
import ru.otus.contacts.data.Contact

private const val DEFAULT_COUNT = 30
private const val MAX_COUNT = 100
private val faker = Faker()

fun getContacts(number: Int? = null): Array<Contact> = Array((number ?: DEFAULT_COUNT).coerceIn(0..MAX_COUNT)) { index ->
    val name = faker.name().fullName()
    Contact(
        id = "contact$index",
        name = name,
        email = faker.internet().emailAddress(),
        phone = faker.phoneNumber().phoneNumber(),
        userPic = getAvatar(name).toString()
    )
}

private fun getAvatar(name: String) = URLBuilder(
    protocol = URLProtocol.HTTPS,
    host = "avatar.iran.liara.run",
    pathSegments = listOf("username"),
    parameters = Parameters.build {
        append("username", name)
    }
).build()