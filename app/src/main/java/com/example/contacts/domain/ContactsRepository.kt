package com.example.contacts.domain

import com.example.contacts.common.Result

interface ContactsRepository {

    suspend fun getContacts(): Result<List<Contact>>

}
