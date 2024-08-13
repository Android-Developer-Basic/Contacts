package ru.otus.contacts

import io.ktor.client.request.accept
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.server.testing.testApplication
import kotlinx.serialization.json.Json
import ru.otus.contacts.data.ContactsData
import ru.otus.contacts.data.ErrorCode
import ru.otus.contacts.data.HttpResponse
import ru.otus.contacts.data.LoginRequest
import ru.otus.contacts.data.SessionClaims
import ru.otus.contacts.data.httpResponseModule
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class ApplicationTest {

    private val json = Json {
        serializersModule = httpResponseModule
    }

    @Test
    fun rootResponds() = testApplication {
        application {
            module()
        }
        val response = client.get("/") {
            accept(ContentType.Application.Json)
        }

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(HttpResponse.Data("Hello World"), json.decodeFromString(response.bodyAsText()) )
    }

    @Test
    fun logsInIfCorrect() = testApplication {
        application {
            module()
        }
        val response = client.post("/login") {
            accept(ContentType.Application.Json)
            contentType(ContentType.Application.Json)
            setBody(
                json.encodeToString(LoginRequest.serializer(), LoginRequest(USERNAME, PASSWORD))
            )
        }
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(
            HttpResponse.Data(SessionClaims(USERNAME, TOKEN)),
            json.decodeFromString<HttpResponse<SessionClaims>>(response.bodyAsText())
        )
    }

    @Test
    fun failsLoginIfIncorrectCredentials() = testApplication {
        application {
            module()
        }
        val response = client.post("/login") {
            accept(ContentType.Application.Json)
            contentType(ContentType.Application.Json)
            setBody(
                json.encodeToString(LoginRequest.serializer(), LoginRequest("cool", "hacker"))
            )
        }
        assertEquals(HttpStatusCode.Forbidden, response.status)
        assertEquals(
            HttpResponse.Error(ErrorCode.FORBIDDEN, ErrorCode.FORBIDDEN.defaultMessage),
            json.decodeFromString<HttpResponse<SessionClaims>>(response.bodyAsText())
        )
    }

    @Test
    fun returnsContactsForAuthorizedUsers() = testApplication {
        application {
            module()
        }
        val response = client.get("/contacts") {
            accept(ContentType.Application.Json)
            bearerAuth(TOKEN)
            parameter("count", 10)
        }
        assertEquals(HttpStatusCode.OK, response.status)
        val contacts = json.decodeFromString<HttpResponse<ContactsData>>(response.bodyAsText())
        assertIs<HttpResponse.Data<ContactsData>>(contacts)
        assertEquals(10, contacts.data.contacts.size)
    }

    @Test
    fun failsToGetContactsForNonAuthorizedUser() = testApplication {
        application {
            module()
        }
        val response = client.get("/contacts") {
            accept(ContentType.Application.Json)
            parameter("count", 10)
        }
        assertEquals(HttpStatusCode.Unauthorized, response.status)
        assertEquals(
            HttpResponse.Error(ErrorCode.UNAUTHORIZED, ErrorCode.UNAUTHORIZED.defaultMessage),
            json.decodeFromString<HttpResponse<ContactsData>>(response.bodyAsText())
        )
    }
}