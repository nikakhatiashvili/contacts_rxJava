package com.example.contacts.presentation.contacts

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.contacts.R
import com.example.contacts.databinding.FragmentContactsBinding
import com.example.contacts.presentation.contacts.adapter.ContactsAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ContactsFragment : Fragment() {

    private var _binding: FragmentContactsBinding? = null
    private val binding get() = _binding!!

    private val viewModel:ContactsViewModel by viewModels()

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
            viewModel.contactsState.collect {
                it.apply(adapter, binding.loadingProgressBar)
            }
        }
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
                showPermissionNeedsToBeGrantedSnackbar()
            }
        }
    }

    private fun showPermissionNeedsToBeGrantedSnackbar() {
        Snackbar.make(
            binding.root,
            getString(R.string.permission_message_from_snackbar),
            Snackbar.LENGTH_SHORT
        ).setAction(getString(R.string.settings)) {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri =
                Uri.fromParts(getString(R.string.package_text), requireActivity().packageName, null)
            intent.data = uri
            startActivity(intent)
        }.show()
    }

    private fun showPermissionNeedsToBeGrantedDialog() {
        MaterialAlertDialogBuilder(requireContext()).setTitle(
            getString(R.string.permission_required),
        )
            .setMessage(getString(R.string.permission_req_dialog))
            .setNegativeButton(getString(R.string.deny_from_dialog)) { dialog, _ ->
                showPermissionNeedsToBeGrantedSnackbar()
                dialog.dismiss()
            }.setPositiveButton(getString(R.string.accept_from_dialog)) { d, _ ->
                requestContactsPermission()
                d.dismiss()
            }.show()
    }
}
