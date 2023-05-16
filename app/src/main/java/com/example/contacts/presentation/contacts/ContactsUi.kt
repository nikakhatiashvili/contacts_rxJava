package com.example.contacts.presentation.contacts

import android.view.View
import android.widget.Toast
import com.example.contacts.domain.Contact
import com.example.contacts.presentation.contacts.adapter.ContactsAdapter

interface ContactsUi {

    fun apply(adapter: ContactsAdapter, progressBar: View)

    object Empty : ContactsUi {
        override fun apply(adapter: ContactsAdapter, progressBar: View) = Unit
    }

    class Loading : ContactsUi {
        override fun apply(adapter: ContactsAdapter, progressBar: View) {
            progressBar.visibility = View.VISIBLE
        }
    }

    class SuccessUi(private val list: List<Contact>) : ContactsUi {
        override fun apply(adapter: ContactsAdapter, progressBar: View) {
            val transformedList = mutableListOf<ContactsAdapter.ListItem>()
            var currentHeader: Char? = null
            for (contact in list) {
                val firstChar = contact.name.first().toUpperCase()
                if (firstChar != currentHeader?.toUpperCase()) {
                    transformedList.add(ContactsAdapter.ListItem.GroupItem(firstChar.toString()))
                    currentHeader = firstChar
                }
                transformedList.add(ContactsAdapter.ListItem.UiContact(contact))
            }
            adapter.submitList(transformedList)
            progressBar.visibility = View.GONE
        }
    }
    class ErrorUi(private val message: String?) : ContactsUi {
        override fun apply(adapter: ContactsAdapter, progressBar: View) {
            Toast.makeText(progressBar.context, message, Toast.LENGTH_SHORT).show()
            progressBar.visibility = View.GONE
        }
    }

}
