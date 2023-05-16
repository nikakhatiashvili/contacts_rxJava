package com.example.contacts.di

import com.example.contacts.data.AddContactRepositoryImpl
import com.example.contacts.data.ContactsRepositoryImpl
import com.example.contacts.domain.AddContactRepository
import com.example.contacts.domain.ContactsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface ContactsModule {

    @Binds
    fun bindsContactsRepository(contactsRepositoryImpl: ContactsRepositoryImpl): ContactsRepository

    @Binds
    fun bindsAddContactRepository(addContactRepositoryImpl: AddContactRepositoryImpl): AddContactRepository
}
