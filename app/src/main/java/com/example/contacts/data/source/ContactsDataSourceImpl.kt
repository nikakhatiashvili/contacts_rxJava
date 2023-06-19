package com.example.contacts.data.source

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.ContactsContract
import com.example.contacts.domain.model.Contact
import com.example.contacts.domain.repository.source.ContactsDataSource
import java.lang.Exception

class ContactsDataSourceImpl(
    private val context: Context
) : ContactsDataSource {

    override suspend fun getContacts(): List<Contact> {
        return getNamePhoneDetails()
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
                val pictureUri: Uri?

                val id =try {
                    cur.getString(cur.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NAME_RAW_CONTACT_ID))
                }catch (e:Exception){""}
                val name = try {
                    cur.getString(cur.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                }catch (e:Exception){""}

                val number = try {
                    cur.getString(cur.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER))
                }catch (e:Exception) {""}

                val pictureUrl:String = try {
                    cur.getString(cur.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Photo.PHOTO_URI))
                }catch (e:Exception){ "" }

                pictureUri = Uri.parse(pictureUrl)
                contacts.add(Contact(id, name, number,pictureUri,null))
            }
        }
        return contacts
    }
}
