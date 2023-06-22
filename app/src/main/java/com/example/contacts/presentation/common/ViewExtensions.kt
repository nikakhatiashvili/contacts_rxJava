package com.example.contacts.presentation.common

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

fun View.snack(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_SHORT)
        .setAction("Settings") {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", this.context.packageName, null)
            intent.data = uri
            this.context.startActivity(intent)
        }.show()
}

fun View.gone(){
    this.visibility = View.GONE
}

fun View.visible(){
    this.visibility = View.VISIBLE
}


suspend fun <T> collect(flow: Flow<T>, onCollect: suspend (T) -> Unit) {
    flow.collectLatest(onCollect)
}
fun <T> Fragment.collectFlow(flow: Flow<T>, onCollect: suspend (T) -> Unit) =
    viewLifecycleOwner.lifecycleScope.launch {
        flow.flowWithLifecycle(
            viewLifecycleOwner.lifecycle,
            Lifecycle.State.STARTED
        ).collectLatest(onCollect)
    }