package com.example.contacts.data

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.provider.ContactsContract
import com.example.contacts.Result
import com.example.contacts.domain.Contact
import com.example.contacts.domain.ContactsRepository
import com.example.contacts.presentation.common.isValidEmail
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ContactsRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : ContactsRepository {
    override suspend fun getContacts(): Result<List<Contact>> {
        return try {
            val data = getNamePhoneDetails()
            Result.Success(data)
        } catch (e: Exception) {
            Result.Exception(e)
        }
    }

    private fun getNamePhoneDetails(): MutableList<Contact> {
        val contacts = mutableListOf<Contact>()
        val cr: ContentResolver = context.contentResolver
        val cur = cr.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
            null, null, null
        )
        if (cur != null && cur.count > 0) {
            while (cur.moveToNext()) {
                val id =
                    cur.getString(cur.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NAME_RAW_CONTACT_ID))
                val name =
                    cur.getString(cur.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                val number =
                    cur.getString(cur.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER))
                contacts.add(Contact(id, name, number))
            }
        }
        contacts.forEach {
            it.filterNumber()
        }
        contacts.sortBy { it.name.first().toUpperCase()}
        return contacts
    }


}
