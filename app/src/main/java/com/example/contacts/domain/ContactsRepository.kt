package com.example.contacts.domain

import com.example.contacts.Result

interface ContactsRepository {

    suspend fun getContacts(): Result<List<Contact>>
}
