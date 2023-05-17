package com.example.contacts.presentation.contacts.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.contacts.databinding.ContactItemBinding

class ContactsViewHolder(
    private val binding: ContactItemBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: ContactsAdapter.ListItem.UiContact) {
        binding.nameTv.text = item.contact.name
        binding.numberTv.text = item.contact.number
    }
}
