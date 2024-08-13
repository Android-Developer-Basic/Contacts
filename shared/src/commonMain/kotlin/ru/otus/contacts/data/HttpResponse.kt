package ru.otus.contacts.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

/**
 * Common HTTP response data
 */
@Serializable
sealed class HttpResponse<out T: Any> {
    abstract val isSuccessful: Boolean
    abstract val message: String

    /**
     * Ok with data
     */
    @Serializable
    @SerialName("Data")
    data class Data<out T: Any>(val data: T, override val message: String = "OK") : HttpResponse<T>() {
        @Transient
        override val isSuccessful: Boolean = true
    }

    /**
     * Error
     */
    @Serializable
    @SerialName("Error")
    data class Error(val code: ErrorCode, override val message: String) : HttpResponse<Nothing>() {
        @Transient
        override val isSuccessful: Boolean = false
    }
}

enum class ErrorCode(val defaultMessage: String) {
    NOT_FOUND("NOT FOUND"),
    BAD_REQUEST("BAD REQUEST"),
    CONFLICT("CONFLICT"),
    UNAUTHORIZED("UNAUTHORIZED"),
    FORBIDDEN("FORBIDDEN")
}