//package com.example.contacts.presentation.contacts
//
//class test {
//    private fun getContacts() {
//        if (ActivityCompat.checkSelfPermission(
//                requireContext(), Manifest.permission.READ_CONTACTS
//            ) == PackageManager.PERMISSION_GRANTED
//            && ActivityCompat.checkSelfPermission(
//                requireContext(), Manifest.permission.WRITE_CONTACTS
//            ) == PackageManager.PERMISSION_GRANTED
//        ) {
//            viewModel.getContacts()
//        } else {
//            requestContactsPermission()
//        }
//    }
//    private fun requestContactsPermission() {
//        contactsPermissionLauncher.launch(
//            arrayOf(
//                Manifest.permission.READ_CONTACTS,
//                Manifest.permission.WRITE_CONTACTS
//            )
//        )
//    }
//    private val contactsPermissionLauncher = registerForActivityResult(
//        ActivityResultContracts.RequestMultiplePermissions()
//    ) { permissions ->
//        if (permissions.all { it.value }) {
//            viewModel.getContacts()
//        } else {
//            if (shouldShowRequestPermissionRationale(
//                    Manifest.permission.READ_CONTACTS
//                )
//                || shouldShowRequestPermissionRationale(
//                    Manifest.permission.WRITE_CONTACTS
//                )
//            ) {
//                showPermissionNeedsToBeGrantedDialog()
//            } else {
//                showPermissionNeedsToBeGrantedSnackbar()
//            }
//        }
//    }
//    private fun showPermissionNeedsToBeGrantedSnackbar() {
//        Snackbar.make(
//            binding.root,
//            getString(R.string.permission_message_from_snackbar),
//            Snackbar.LENGTH_SHORT
//        ).setAction(getString(R.string.settings)) {
//            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
//            val uri = Uri.fromParts("package", activity!!.packageName, null)
//            intent.data = uri
//            startActivity(intent)
//        }.show()
//    }
//    private fun showPermissionNeedsToBeGrantedDialog() {
//        MaterialAlertDialogBuilder(requireContext()).setTitle(
//            getString(R.string.permission_required),
//        )
//            .setMessage(getString(R.string.permission_req_dialog))
//            .setNegativeButton(getString(R.string.deny_from_dialog)) { dialog, _ ->
//                dialog.dismiss()
//            }.setPositiveButton(getString(R.string.accept_from_dialog)) { d, _ ->
//                requestContactsPermission()
//                d.dismiss()
//            }.show()
//    }
//}
