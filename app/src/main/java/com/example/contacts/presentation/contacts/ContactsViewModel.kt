package com.example.contacts.presentation.contacts

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contacts.common.Result
import com.example.contacts.domain.usecase.GetContactsUseCase
import com.example.contacts.presentation.contacts.adapter.ContactsAdapter
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class ContactsViewModel(
    private val getContactsUseCase: GetContactsUseCase,
) : ViewModel() {

    private val _contactsState = MutableStateFlow<ContactsUi>(ContactsUi.Empty)
    val contactsState = _contactsState.asStateFlow()

    private val disposables = CompositeDisposable()

    private var searchJob: Job? = null

     val responseCharacters = MutableLiveData<List<ContactsAdapter.ListItem>>()



    companion object {
        const val DELAY = 500L
    }

    fun getContacts() {
        disposables.add(getContactsUseCase.getContacts()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { res ->
                responseCharacters.value = res
            }
        )
    }

    fun filterContacts(str: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch(Dispatchers.IO) {
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

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

}
