package ru.otus.contacts.state

import ru.otus.contacts.data.LoginFormData

interface ContactsFactory {
    fun login(loginFormData: LoginFormData = LoginFormData()): ContactsState
}

class ContactsFactoryImpl : ContactsFactory {
    private val context: ContactsContext = object : ContactsContext {
        override val factory: ContactsFactory = this@ContactsFactoryImpl
    }

    override fun login(loginFormData: LoginFormData): ContactsState = LoginState(
        context,
        loginFormData
    )
}