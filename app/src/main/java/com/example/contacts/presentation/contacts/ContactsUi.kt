package com.example.contacts.presentation.contacts

import android.view.View
import android.widget.Toast
import com.example.contacts.presentation.common.gone
import com.example.contacts.presentation.common.visible
import com.example.contacts.presentation.contacts.adapter.ContactsAdapter

interface ContactsUi {

    fun apply(adapter: ContactsAdapter, progressBar: View)

    object Empty : ContactsUi {
        override fun apply(
            adapter: ContactsAdapter,
            progressBar: View,
        ) {
        }
    }

    class Loading : ContactsUi {
        override fun apply(
            adapter: ContactsAdapter,
            progressBar: View,
        ) {
            progressBar.visible()
        }
    }

    class SuccessUi(private val list: List<ContactsAdapter.ListItem>) : ContactsUi {
        override fun apply(
            adapter: ContactsAdapter,
            progressBar: View,
        ) {
            adapter.submitList(list)
            progressBar.visibility = View.GONE
        }
    }

    class ErrorUi(private val message: String?) : ContactsUi {
        override fun apply(
            adapter: ContactsAdapter,
            progressBar: View,
        ) {
            Toast.makeText(progressBar.context, message, Toast.LENGTH_SHORT).show()
            progressBar.gone()
        }
    }
}
