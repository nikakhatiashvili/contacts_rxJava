package com.example.contacts.presentation

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.contacts.databinding.ContactItemBinding
import com.example.contacts.domain.Contact

class ContactsAdapter : RecyclerView.Adapter<ContactsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ContactItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    var data: List<Contact> = emptyList()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class ViewHolder(
        private val binding: ContactItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        private lateinit var currentData: Contact
        fun bind() {
            currentData = data[adapterPosition]
            binding.nameTv.text = currentData.name
            binding.numberTv.text = currentData.number
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount() = data.size
}
