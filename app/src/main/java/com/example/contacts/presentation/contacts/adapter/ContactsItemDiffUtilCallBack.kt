package com.example.contacts.presentation.contacts.adapter

import androidx.recyclerview.widget.DiffUtil

class ContactsItemDiffUtilCallBack : DiffUtil.ItemCallback<ContactsAdapter.ListItem>() {
    override fun areItemsTheSame(
        oldItem: ContactsAdapter.ListItem,
        newItem: ContactsAdapter.ListItem
    ): Boolean {
        return when {
            oldItem is ContactsAdapter.ListItem.GroupItem && newItem is
                    ContactsAdapter.ListItem.GroupItem -> oldItem.id == newItem.id

            oldItem is ContactsAdapter.ListItem.UiContact && newItem is
                    ContactsAdapter.ListItem.UiContact -> oldItem.contact.id == newItem.contact.id

            else -> false
        }
    }

    override fun areContentsTheSame(
        oldItem: ContactsAdapter.ListItem,
        newItem: ContactsAdapter.ListItem
    ): Boolean {
        return oldItem == newItem
    }
}
