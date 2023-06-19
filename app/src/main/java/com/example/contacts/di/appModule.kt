package com.example.contacts.di

import com.example.contacts.data.AddContactRepositoryImpl
import com.example.contacts.data.ContactsRepositoryImpl
import com.example.contacts.data.source.AddContactDataSourceImpl
import com.example.contacts.data.source.ContactsDataSourceImpl
import com.example.contacts.domain.repository.AddContactRepository
import com.example.contacts.domain.repository.ContactsRepository
import com.example.contacts.domain.repository.source.AddContactDataSource
import com.example.contacts.domain.repository.source.ContactsDataSource
import com.example.contacts.domain.usecase.AddContactUseCase
import com.example.contacts.domain.usecase.GetContactsUseCase
import com.example.contacts.presentation.contacts.ContactsViewModel
import com.example.contacts.presentation.new_contact.NewContactViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<AddContactDataSource> {
        AddContactDataSourceImpl(androidContext())
    }
    single<ContactsDataSource> {
        ContactsDataSourceImpl(androidContext())
    }
    single<AddContactRepository> {
        AddContactRepositoryImpl(get())
    }

    single<ContactsRepository> {
        ContactsRepositoryImpl(get())
    }

    single {
        AddContactUseCase(get())
    }
    single {
        GetContactsUseCase(get())
    }

    viewModel {ContactsViewModel(get()) }
    viewModel {NewContactViewModel(get()) }
}