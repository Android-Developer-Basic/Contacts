package ru.otus.contacts.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.URLBuilder
import io.ktor.http.URLProtocol
import io.ktor.http.appendPathSegments
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import ru.otus.contacts.data.HttpResponse
import ru.otus.contacts.data.LoginRequest
import ru.otus.contacts.data.SessionClaims
import ru.otus.contacts.data.httpResponseModule
import ru.otus.contacts.getPlatform

interface ContactsApi {
    suspend fun login(credentials: LoginRequest): HttpResponse<SessionClaims>
}

class ContactsApiImpl : ContactsApi {
    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json { serializersModule = httpResponseModule })
        }
    }

    override suspend fun login(credentials: LoginRequest): HttpResponse<SessionClaims> {
        val result = withContext(Dispatchers.IO) {
            httpClient.post {
                platformUrl(listOf("login"))
                contentType(ContentType.Application.Json)
                setBody(credentials)
            }
        }
        return result.body()
    }

    companion object {
        private fun HttpRequestBuilder.platformUrl(endpoint: List<String>, extraUrl: URLBuilder.() -> Unit = {}) {
            url {
                host = getPlatform().host
                port = getPlatform().port
                protocol  = URLProtocol.HTTP
                appendPathSegments(endpoint)
                extraUrl()
            }
        }
    }
}