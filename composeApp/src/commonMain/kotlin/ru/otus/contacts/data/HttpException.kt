package ru.otus.contacts.data

class HttpException(val code: ErrorCode, message: String) : RuntimeException(message)