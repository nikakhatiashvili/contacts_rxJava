package com.example.contacts.domain.usecase

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.example.contacts.common.Result
import com.example.contacts.domain.model.Contact
import com.example.contacts.domain.repository.ContactsRepository
import com.example.contacts.presentation.contacts.adapter.ContactsAdapter
import io.reactivex.rxjava3.core.Observable
import java.util.Random

class GetContactsUseCase(
    private val contactsRepository: ContactsRepository
) {

    fun getContacts(): Observable<List<ContactsAdapter.ListItem>> {
        val data = contactsRepository.getContacts()
        return when (data) {
            is Result.Success -> {
                val list = sortList(data.data)
                Observable.just(getTransformedList(list))
            }

            is Result.Error -> {
                Observable.empty()
//                Result.Error(data.message)
            }

            is Result.Exception -> {
                Observable.error(data.e)
//                Result.Exception(data.e)
            }
        }
    }

    suspend fun getFilteredContacts(str: String): Result<List<ContactsAdapter.ListItem>> {
        return when (val data = contactsRepository.getContacts()) {
            is Result.Success -> {
                val list = sortList(data.data)
                Result.Success(filter(str, list))
            }

            is Result.Error -> {
                Result.Error(data.message)
            }

            is Result.Exception -> {
                Result.Exception(data.e)
            }
        }
    }

    private fun filter(str: String, list: List<Contact>): List<ContactsAdapter.ListItem> {
        return if (!str.contains("[0-9]".toRegex())) {
            getTransformedList(list.filter { it.name.contains(str, ignoreCase = true) })
        } else {
            getTransformedList(list.filter {
                it.number.replace("\\s".toRegex(), "").contains(str, ignoreCase = true)
            }
            )
        }
    }

    private fun sortList(data: List<Contact>): List<Contact> {
        val sortedList = data.toMutableList()

        sortedList.forEach {
            it.filterNumber()
            if (it.image.toString().isNullOrEmpty()) {
                val color = generateRandomColor()
                it.bitmap =
                    generateLetterBitmap(it.name.first().toUpperCase(), color, Color.WHITE, 45f)
            }
        }
        sortedList.sortBy { it.name.first().toUpperCase() }
        return sortedList
    }

    private fun getTransformedList(data: List<Contact>): List<ContactsAdapter.ListItem> {
        val transformedList = mutableListOf<ContactsAdapter.ListItem>()
        var currentHeader: Char? = null
        for (contact in data) {
            val firstChar = contact.name.first().toUpperCase()
            if (firstChar != currentHeader?.toUpperCase()) {
                transformedList.add(ContactsAdapter.ListItem.GroupItem(firstChar.toString()))
                currentHeader = firstChar
            }
            transformedList.add(ContactsAdapter.ListItem.UiContact(contact))
        }
        return transformedList.toList()
    }

    private fun generateRandomColor(): Int {
        val random = Random()
        val r = random.nextInt(256)
        val g = random.nextInt(256)
        val b = random.nextInt(256)
        return Color.rgb(r, g, b)
    }

    private fun generateLetterBitmap(
        letter: Char,
        backgroundColor: Int,
        textColor: Int,
        textSize: Float
    ): Bitmap {
        val bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint = Paint().apply {
            color = backgroundColor
            style = Paint.Style.FILL
        }
        canvas.drawPaint(paint)

        paint.color = textColor
        paint.textSize = textSize
        paint.textAlign = Paint.Align.CENTER
        val x = canvas.width / 2F
        val y = (canvas.height / 2F) - ((paint.descent() + paint.ascent()) / 2F)
        canvas.drawText(letter.toString(), x, y, paint)

        return bitmap
    }
}
