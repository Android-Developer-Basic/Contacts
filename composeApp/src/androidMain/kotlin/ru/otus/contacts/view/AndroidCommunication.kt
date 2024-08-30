package ru.otus.contacts.view

import android.content.Context
import android.content.Intent
import android.net.Uri

class AndroidCommunication(private val context: Context) : Communication {
    override fun sendMail(address: String) {
        startIntent(Intent.ACTION_SENDTO, Uri.parse("mailto:$address"))
    }

    override fun callPhone(phone: String) {
        startIntent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
    }

    private fun startIntent(action: String, url: Uri) {
        val intent = Intent(
            action,
            url
        )
        context.startActivity(intent)
    }
}