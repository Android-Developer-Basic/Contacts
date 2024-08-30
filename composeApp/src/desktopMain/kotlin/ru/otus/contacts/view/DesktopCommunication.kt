package ru.otus.contacts.view

import io.github.aakira.napier.Napier

object DesktopCommunication : Communication {
    override fun sendMail(address: String) {
        Napier.w { "Sending mail is not supported on desktop" }
    }

    override fun callPhone(phone: String) {
        Napier.w { "Calling phone is not supported on desktop" }
    }
}