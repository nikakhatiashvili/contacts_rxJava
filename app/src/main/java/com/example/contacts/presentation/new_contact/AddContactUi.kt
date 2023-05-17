package com.example.contacts.presentation.new_contact

import android.view.View
import android.widget.Toast
import androidx.navigation.NavController
import com.example.contacts.R

interface AddContactUi {

    fun apply(navController: NavController, progressBar: View)

    object Empty : AddContactUi {
        override fun apply(navController: NavController, progressBar: View) = Unit
    }

    class Loading : AddContactUi {
        override fun apply(navController: NavController, progressBar: View) {
            progressBar.visibility = View.VISIBLE
        }
    }

    class SuccessUi(private val message: String?) : AddContactUi {
        override fun apply(navController: NavController, progressBar: View) {
            Toast.makeText(progressBar.context, message, Toast.LENGTH_SHORT).show()
            progressBar.visibility = View.GONE
            navController.navigate(R.id.action_newContactFragment_to_contactsFragment)
        }
    }

    class ErrorUi(private val message: String?) : AddContactUi {
        override fun apply(navController: NavController, progressBar: View) {
            Toast.makeText(progressBar.context, message, Toast.LENGTH_SHORT).show()
            progressBar.visibility = View.GONE
        }
    }

}
