package com.example.contacts.data

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.provider.ContactsContract
import android.util.Log
import com.example.contacts.Result
import com.example.contacts.domain.Contact
import com.example.contacts.domain.ContactsRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import java.lang.Exception
import javax.inject.Inject

class ContactsRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : ContactsRepository {
    override suspend fun getContacts(): Result<List<Contact>> {
        try {
            val data = getNamePhoneDetails()
            if (data == null) return Result.Error("null")
            else return Result.Success(data)
        }catch (e:Exception){
            return Result.Exception(e)
        }
    }

    @SuppressLint("Range")
    fun getNamePhoneDetails(): MutableList<Contact>? {
        val names = mutableListOf<Contact>()
        val cr: ContentResolver = context.getContentResolver()

        val cursor: Cursor? = cr.query(
            ContactsContract.Contacts.CONTENT_URI, null, null, null, null
        )
        val cur = cr.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
            null, null, null
        )
        if (cur!!.count > 0) {
            while (cur.moveToNext()) {
                val id =
                    cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NAME_RAW_CONTACT_ID))
                val name =
                    cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                val number =
                    cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                names.add(Contact(id, name, number))
            }
        }
        return names
    }
}