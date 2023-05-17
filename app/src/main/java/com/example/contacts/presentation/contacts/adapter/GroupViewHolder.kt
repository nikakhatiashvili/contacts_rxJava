package com.example.contacts.presentation.contacts.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.contacts.databinding.GroupItemBinding

class GroupViewHolder(private val binding: GroupItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(groupItem: ContactsAdapter.ListItem.GroupItem) {
        binding.groupTv.text = groupItem.nameHeader
    }
}
