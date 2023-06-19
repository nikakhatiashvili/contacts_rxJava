package com.example.contacts.presentation.contacts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contacts.common.Result
import com.example.contacts.domain.usecase.GetContactsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ContactsViewModel (
    private val getContactsUseCase: GetContactsUseCase,
) : ViewModel() {

    private val _contactsState = MutableStateFlow<ContactsUi>(ContactsUi.Empty)
    val contactsState = _contactsState.asStateFlow()

    private var searchJob: Job? = null

    private var count = 2

    companion object{
        const val DELAY = 500L
    }

    fun getContacts() {
        count++
        viewModelScope.launch(Dispatchers.IO) {
            _contactsState.emit(ContactsUi.Loading())
            when (val res = getContactsUseCase.getContacts()) {
                is Result.Success -> _contactsState.emit(ContactsUi.SuccessUi(res.data))
                is Result.Error -> _contactsState.emit(ContactsUi.ErrorUi(res.message))
                is Result.Exception -> _contactsState.emit(ContactsUi.ErrorUi("res.e"))
            }

        }
    }

    fun filterContacts(str: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch(Dispatchers.IO) {
            delay(DELAY)
            when (val res = getContactsUseCase.getFilteredContacts(str)) {
                is Result.Success -> {
                    _contactsState.emit(
                        ContactsUi.SuccessUi(res.data)
                    )
                }
                is Result.Error -> _contactsState.emit(ContactsUi.ErrorUi(res.message))
                is Result.Exception -> _contactsState.emit(ContactsUi.ErrorUi("s"))
            }
        }
    }

}
