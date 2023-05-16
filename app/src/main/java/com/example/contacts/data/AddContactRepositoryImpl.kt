package com.example.contacts.data

import android.content.ContentProviderOperation
import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.ContactsContract
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import com.example.contacts.Result
import com.example.contacts.domain.AddContactRepository
import com.example.contacts.presentation.common.isValidEmail
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.ByteArrayOutputStream
import javax.inject.Inject


class AddContactRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : AddContactRepository {

    override suspend fun addContact(
        name: String,
        lastName: String,
        number: String,
        email: String,
        image: Uri?
    ): Result<String> {
        if (name.isEmpty() || number.isEmpty() || email.isEmpty()) {
            return Result.Error("Fill all the fields")
        }
        if (!email.isValidEmail()) {
            return Result.Error("Email is not valid")
        }
        return try {
            val data = saveContact(name, lastName, number, email, image)
            if (data) Result.Success("succesfully added ") else Result.Error("sad")
        } catch (e: Exception) {
            Result.Exception(e)
        }
    }


    private fun saveContact(
        name: String,
        lastName: String,
        number: String,
        email: String,
        image: Uri?
    ): Boolean {
        val cr: ContentResolver = context.contentResolver
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            insertContact(cr,name,lastName,number,email,image)
        } else {
           false
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun insertContact(
        contactAdder: ContentResolver,
        firstName: String?,
        lastName: String?,
        mobileNumber: String?,
        email: String,
        image: Uri?,
        ): Boolean {
        val ops = ArrayList<ContentProviderOperation>()

        val rawContactId = ops.size
        ops.add(ContentProviderOperation.newInsert(
            ContactsContract.RawContacts.CONTENT_URI)
            .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
            .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
            .build()
        )

        ops.add(
            ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactId)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, firstName?.trim())
                .withValue(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME, lastName?.trim())
                .build()
        )

        ops.add(ContentProviderOperation.
        newInsert(ContactsContract.Data.CONTENT_URI)
            .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
            .withValue(ContactsContract.Data.MIMETYPE,
                ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
            .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, mobileNumber)
            .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
            .build())

        ops.add(
            ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactId)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Email.DATA, email.trim())
                .withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
                .build()
        )

        val imageBytes = imageUriToBytes(image)

        if (imageBytes != null){
            ops.add(ContentProviderOperation.newInsert((ContactsContract.Data.CONTENT_URI))
                .withValueBackReference(ContactsContract.RawContacts.Data.RAW_CONTACT_ID, rawContactId)
                .withValue(ContactsContract.RawContacts.Data.MIMETYPE,ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Photo.PHOTO, imageBytes).build())
        }
        return try {
            contactAdder.applyBatch(ContactsContract.AUTHORITY,ops)
            true
        }catch (e:Exception){
            false
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun imageUriToBytes(
        image:Uri?
    ):ByteArray?{

        val bitmap:Bitmap
        val baos:ByteArrayOutputStream?
        val cr: ContentResolver = context.contentResolver
        return try {
            if (Build.VERSION.SDK_INT > 28){
                bitmap = MediaStore.Images.Media.getBitmap(cr, image)
            }
            else{
                val source = ImageDecoder.createSource(cr, image!!)
                bitmap = ImageDecoder.decodeBitmap(source)
            }

            baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG,50,baos)
            baos.toByteArray()
        }catch (e:java.lang.Exception){
            null
        }
    }



}