package ru.otus.contacts

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.server.auth.AuthenticationConfig
import io.ktor.server.auth.UserIdPrincipal
import io.ktor.server.auth.bearer
import io.ktor.server.request.receiveNullable
import io.ktor.server.response.respond
import io.ktor.util.pipeline.PipelineContext
import org.jetbrains.annotations.VisibleForTesting
import ru.otus.contacts.data.HttpResponse
import ru.otus.contacts.data.LoginRequest
import ru.otus.contacts.data.SessionClaims

@VisibleForTesting
internal const val TOKEN = "token123"
@VisibleForTesting
internal const val USERNAME = "username"
@VisibleForTesting
internal const val PASSWORD = "password"

fun AuthenticationConfig.stubBearer(name: String? = null) {
    bearer(name) {
        realm = name
        authenticate { credential ->
            if (TOKEN == credential.token) {
                UserIdPrincipal(USERNAME)
            } else {
                null
            }
        }
    }
}

suspend fun PipelineContext<*, ApplicationCall>.login() {
    val request = call.receiveNullable<LoginRequest>() ?: throw IllegalArgumentException("Login Request is invalid")
    if (USERNAME == request.username && PASSWORD == request.password) {
        call.respond<HttpResponse<SessionClaims>>(HttpResponse.Data(SessionClaims(request.username, TOKEN)))
    } else {
        call.respond(HttpStatusCode.Forbidden)
    }
}