package com.example.contacts.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contacts.Result
import com.example.contacts.domain.Contact
import com.example.contacts.domain.ContactsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactsViewModel @Inject constructor(
    private val contactsRepository: ContactsRepository
) : ViewModel() {

    private val _contactsState = MutableStateFlow<ContactsUi>(ContactsUi.Empty)
    val contactsState = _contactsState.asStateFlow()

    private var searchJob: Job? = null

    fun getContacts() {
        viewModelScope.launch(Dispatchers.IO) {
            _contactsState.emit(ContactsUi.Loading())
            when (val res = contactsRepository.getContacts()) {
                is Result.Success -> _contactsState.emit(ContactsUi.SuccessUi(res.data))
                is Result.Error -> _contactsState.emit(ContactsUi.ErrorUi(res.message))
                is Result.Exception -> _contactsState.emit(ContactsUi.ErrorUi(res.e.message))
            }

        }
    }

    fun filterContacts(str: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch(Dispatchers.IO) {
            delay(400)
            when (val res = contactsRepository.getContacts()) {
                is Result.Success -> {
                    _contactsState.emit(
                        ContactsUi.SuccessUi(filterContacts(res.data, str))
                    )
                }

                is Result.Error -> _contactsState.emit(ContactsUi.ErrorUi(res.message))
                is Result.Exception -> _contactsState.emit(ContactsUi.ErrorUi(res.e.message))
            }
        }
    }

    private fun filterContacts(data: List<Contact>, str: String) =
        if (!str.contains("[0-9]".toRegex())) {
            data.filter { it.name.contains(str, ignoreCase = true) }
        } else
            data.filter { it.number.replace("\\s".toRegex(), "").contains(str, ignoreCase = true) }

}
