package ru.otus.contacts.state

import ru.otus.contacts.ComposeResourceWrapper
import ru.otus.contacts.ResourceWrapper
import ru.otus.contacts.data.ContactsDataState
import ru.otus.contacts.data.ErrorCode
import ru.otus.contacts.data.LoginFormData
import ru.otus.contacts.data.SessionClaims
import ru.otus.contacts.database.ContactsDbProvider
import ru.otus.contacts.network.ContactsApiImpl
import ru.otus.contacts.usecase.LoadContactsImpl

interface ContactsFactory {
    fun login(loginFormData: LoginFormData = LoginFormData()): ContactsState

    fun loggingIn(loginFormData: LoginFormData): ContactsState
    fun loginError(loginFormData: LoginFormData, code: ErrorCode, message: String): ContactsState

    fun loadingContacts(sessionClaims: SessionClaims): ContactsState
    fun loadingContactsError(sessionClaims: SessionClaims, code: ErrorCode, message: String): ContactsState

    fun contactList(dataState: ContactsDataState): ContactsState

    fun contactCard(
        dataState: ContactsDataState,
        contactId: String
    ): ContactsState
    fun contactCardError(
        dataState: ContactsDataState,
        code: ErrorCode,
        message: String
    ): ContactsState

    fun terminated(): ContactsState
}

class ContactsFactoryImpl(private val dbProvider: ContactsDbProvider) : ContactsFactory {
    private val context: ContactsContext = object : ContactsContext {
        override val factory: ContactsFactory = this@ContactsFactoryImpl
        override val resourceWrapper: ResourceWrapper = ComposeResourceWrapper
    }
    private val api = ContactsApiImpl()

    override fun login(loginFormData: LoginFormData): ContactsState = LoginState(
        context,
        loginFormData
    )

    override fun loggingIn(loginFormData: LoginFormData): ContactsState = LoggingInState(
        context,
        loginFormData,
        api
    )

    override fun loginError(loginFormData: LoginFormData, code: ErrorCode, message: String): ContactsState = ErrorState(
        context,
        code,
        message,
        onBack = { login(loginFormData) },
        onAction = { login(loginFormData) }
    )

    override fun loadingContacts(sessionClaims: SessionClaims): ContactsState = LoadingContactsState(
        context,
        sessionClaims,
        dbProvider,
        LoadContactsImpl(dbProvider, api)
    )

    override fun loadingContactsError(sessionClaims: SessionClaims, code: ErrorCode, message: String): ContactsState = ErrorState (
        context,
        code,
        message,
        onBack = { login(LoginFormData(userName = sessionClaims.username)) },
        onAction = { loadingContacts(sessionClaims) }
    )

    override fun contactList(dataState: ContactsDataState): ContactsState = ContactListState(
        context,
        dataState,
        dbProvider,
        LoadContactsImpl(dbProvider, api)
    )

    override fun contactCard(dataState: ContactsDataState, contactId: String): ContactsState = ContactCardState(
        context,
        dataState,
        contactId,
        dbProvider
    )

    override fun contactCardError(dataState: ContactsDataState, code: ErrorCode, message: String): ContactsState = ErrorState (
        context,
        code,
        message,
        onBack = { contactList(dataState) },
        onAction = { contactList(dataState) }
    )

    override fun terminated(): ContactsState = Terminated()
}