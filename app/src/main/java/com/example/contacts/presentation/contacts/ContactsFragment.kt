package com.example.contacts.presentation.contacts

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.contacts.R
import com.example.contacts.databinding.FragmentContactsBinding
import com.example.contacts.presentation.PaginationRecyclerView
import com.example.contacts.presentation.common.snack
import com.example.contacts.presentation.contacts.adapter.ContactsAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.GlobalContext

class ContactsFragment : Fragment() {

    private var _binding: FragmentContactsBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<ContactsViewModel>()

    private lateinit var adapter: ContactsAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContactsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        bind()
        return root
    }

    private fun bind() {
        getContacts()
        adapter = ContactsAdapter()
        binding.apply {
            contactsRv.adapter = adapter
            contactsRv.layoutManager = LinearLayoutManager(requireContext())

            editText.doAfterTextChanged {
                viewModel.filterContacts(it.toString())
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.responseCharacters.observe(viewLifecycleOwner) {
                adapter.submitList(it)
            }
        }

//        viewLifecycleOwner.lifecycleScope.launch {
//            viewModel.contactsState.collect {
//                it.apply(adapter, binding.loadingProgressBar)
//            }
//        }
        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_contactsFragment_to_newContactFragment)
        }
    }

    private fun getContacts() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.WRITE_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            viewModel.getContacts()
        } else {
            requestContactsPermission()
        }
    }

    private fun requestContactsPermission() {
        contactsPermissionLauncher.launch(
            arrayOf(
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.WRITE_CONTACTS
            )
        )
    }

    private val contactsPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions.all { it.value }) {
            viewModel.getContacts()
        } else {
            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.READ_CONTACTS
                )
                || shouldShowRequestPermissionRationale(
                    Manifest.permission.WRITE_CONTACTS
                )
            ) {
                showPermissionNeedsToBeGrantedDialog()
            } else {
                binding.root.snack(getString(R.string.permission_message_from_snackbar))
            }
        }
    }

    private fun showPermissionNeedsToBeGrantedDialog() {
        MaterialAlertDialogBuilder(requireContext()).setTitle(
            getString(R.string.permission_required),
        )
            .setMessage(getString(R.string.permission_req_dialog))
            .setNegativeButton(getString(R.string.deny_from_dialog)) { dialog, _ ->
                binding.root.snack(getString(R.string.permission_message_from_snackbar))
                dialog.dismiss()
            }.setPositiveButton(getString(R.string.accept_from_dialog)) { d, _ ->
                requestContactsPermission()
                d.dismiss()
            }.show()
    }
}
