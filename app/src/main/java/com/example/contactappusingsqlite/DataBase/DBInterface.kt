package com.example.contactappusingsqlite.DataBase

import com.example.contactappusingsqlite.models.ContactData

interface DBInterface {
    fun addContact(contactData: ContactData)
    fun getContacts(): List<ContactData>
    fun deleteContact(contactData: ContactData)
    fun updateContact(contactData: ContactData): Int
}