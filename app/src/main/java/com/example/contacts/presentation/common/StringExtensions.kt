package com.example.contacts.presentation.common

import android.util.Patterns

fun String?.isValidEmail() = !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()