package com.example.contacts.data

import com.example.contacts.common.Result
import com.example.contacts.domain.model.Contact
import com.example.contacts.domain.repository.ContactsRepository
import com.example.contacts.domain.repository.source.ContactsDataSource

class ContactsRepositoryImpl(
    private val contactsDataSource: ContactsDataSource
) : ContactsRepository {
    override  fun getContacts(): Result<List<Contact>> {
        return try {
            val data = contactsDataSource.getContacts()
            Result.Success(data)
        } catch (e: Exception) {
            Result.Exception(e)
        }
    }
}
