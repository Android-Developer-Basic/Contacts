package ru.otus.contacts.view

interface Communication {
    fun sendMail(address: String)
    fun callPhone(phone: String)
}