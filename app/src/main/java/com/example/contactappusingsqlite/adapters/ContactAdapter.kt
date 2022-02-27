package com.example.contactappusingsqlite.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.contactappusingsqlite.databinding.ItemRvBinding
import com.example.contactappusingsqlite.models.ContactData

interface PopupClick {
    fun popupClick(contact: ContactData, position: Int, view: View)
    fun itemClick(item: ContactData)
}

class ContactAdapter(val list: List<ContactData>, val popupClick: PopupClick) :
    RecyclerView.Adapter<ContactAdapter.VH>() {

    inner class VH(var binding: ItemRvBinding) : RecyclerView.ViewHolder(binding.root) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(ItemRvBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.binding.contactName.text = list[position].contact_name
        holder.binding.contactNumber.text = list[position].contact_number

        holder.binding.popup.setOnClickListener {
            popupClick.popupClick(list[position], position, it)
        }

        holder.binding.itemCard.setOnClickListener {
            popupClick.itemClick(list[position])
        }
    }

    override fun getItemCount(): Int = list.size
}
