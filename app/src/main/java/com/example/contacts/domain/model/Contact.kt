package com.example.contacts.domain.model

import android.graphics.Bitmap
import android.net.Uri

data class Contact(
    val id: String,
    val name: String = "",
    var number: String = "",
    val image:Uri?,
    var bitmap: Bitmap?
) {

    fun filterNumber(): String {
        return this.number.replace(Regex("[^0-9\\s]"), "").also { this.number = it }
    }
}
