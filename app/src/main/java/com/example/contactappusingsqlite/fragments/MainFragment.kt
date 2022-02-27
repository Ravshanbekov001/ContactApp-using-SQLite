package com.example.contactappusingsqlite.fragments

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.example.contactappusingsqlite.DataBase.DBHelper
import com.example.contactappusingsqlite.R
import com.example.contactappusingsqlite.adapters.ContactAdapter
import com.example.contactappusingsqlite.adapters.PopupClick
import com.example.contactappusingsqlite.databinding.DeleteDialogItemBinding
import com.example.contactappusingsqlite.databinding.FragmentMainBinding
import com.example.contactappusingsqlite.databinding.ItemDialogBinding
import com.example.contactappusingsqlite.models.ContactData


class MainFragment : Fragment(), PopupClick {

    lateinit var binding: FragmentMainBinding

    lateinit var list: ArrayList<ContactData>
    lateinit var contactAdapter: ContactAdapter
    lateinit var searchRv: ContactAdapter

    lateinit var dbHelper: DBHelper
    var searchResultsList = ArrayList<ContactData>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        binding = FragmentMainBinding.inflate(layoutInflater)

        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchRv = ContactAdapter(searchResultsList, this)
        binding.searchRv.adapter = searchRv

        dbHelper = DBHelper(requireActivity())

        list = dbHelper.getContacts() as ArrayList<ContactData>

        addContact()
        search()
    }


    override fun popupClick(contact: ContactData, position: Int, view: View) {
        val popupMenu = PopupMenu(requireActivity(), view)
        popupMenu.inflate(R.menu.popup_menu)

        popupMenu.setOnMenuItemClickListener { item ->
            when (item?.itemId) {
                R.id.edit -> {
                    val customDialog = AlertDialog.Builder(requireActivity()).create()
                    val bindingDialog = ItemDialogBinding.inflate(layoutInflater)
                    customDialog.setView(bindingDialog.root)
                    customDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

                    bindingDialog.etName.setText(contact.contact_name)
                    bindingDialog.etNumber.setText(contact.contact_number)

                    bindingDialog.submit.setOnClickListener {
                        contact.contact_name = bindingDialog.etName.text.toString()
                        contact.contact_number =
                            bindingDialog.etNumber.text.toString()
                        dbHelper.updateContact(contact)
                        list[position - 1] = contact        // IndexBoundOfException bergani uchun [position-1] qildim
                        contactAdapter.notifyItemChanged(position)
                        Toast.makeText(requireActivity(), "Submitted", Toast.LENGTH_SHORT).show()
                        customDialog.dismiss()
                    }

                    customDialog.show()
                }

                R.id.delete -> {
                    val customDialog = AlertDialog.Builder(requireActivity()).create()
                    val bindingDeleteDialog =
                        DeleteDialogItemBinding.inflate(layoutInflater)
                    customDialog.setView(bindingDeleteDialog.root)

                    customDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

                    bindingDeleteDialog.no.setOnClickListener {
                        customDialog.dismiss()
                    }

                    bindingDeleteDialog.yes.setOnClickListener {
                        dbHelper.deleteContact(contact)
                        list.remove(contact)
                        contactAdapter.notifyItemRemoved(list.size)
                        contactAdapter.notifyItemRangeChanged(position, list.size)
                        onResume()
                        customDialog.dismiss()
                    }

                    customDialog.show()
                }
            }
            true
        }
        popupMenu.show()
    }

    private fun addContact() {
        binding.fabAdd.setOnClickListener {
            val customDialog = AlertDialog.Builder(requireActivity()).create()
            val bindingDialog = ItemDialogBinding.inflate(layoutInflater)
            customDialog.setView(bindingDialog.root)
            customDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

            bindingDialog.submit.setOnClickListener {
                if (bindingDialog.etName.text!!.isNotEmpty() && bindingDialog.etNumber.text!!.isNotEmpty()) {
                    val name = bindingDialog.etName.text.toString().trim()
                    val number = bindingDialog.etNumber.text.toString().trim()
                    val contact = ContactData(name, number)
                    dbHelper.addContact(contact)
                    Toast.makeText(requireActivity(), "Submitted", Toast.LENGTH_SHORT).show()
                    onResume()
                    customDialog.dismiss()
                } else {
                    Toast.makeText(requireActivity(),
                        "Fill in the required area!",
                        Toast.LENGTH_SHORT).show()
                }

            }
            customDialog.show()
        }
    }

    override fun onResume() {
        super.onResume()
        val list = ArrayList<ContactData>()
        val showContacts = dbHelper.getContacts()
        list.addAll(showContacts)

        contactAdapter = ContactAdapter(list, this)
        binding.rvContact.adapter = contactAdapter
    }

    private fun search() {
        binding.search.addTextChangedListener {
            if (it.toString().isEmpty()) {
                binding.rvContact.visibility = View.VISIBLE
                binding.searchRv.visibility = View.INVISIBLE
            } else {
                searchResultsList.clear()
                searchResultsList.addAll(dbHelper.searchContact(it.toString()))
                searchRv.notifyDataSetChanged()

                binding.rvContact.visibility = View.INVISIBLE
                binding.searchRv.visibility = View.VISIBLE
            }
        }
    }

    override fun itemClick(item: ContactData) {
        call(item)
    }

    private fun call(item: ContactData) {
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + item.contact_number))
        startActivity(intent)
    }
}
