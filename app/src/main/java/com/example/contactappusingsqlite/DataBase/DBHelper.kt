package com.example.contactappusingsqlite.DataBase

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.contactappusingsqlite.DataBase.Constants.DB_NAME
import com.example.contactappusingsqlite.DataBase.Constants.DB_VERSION
import com.example.contactappusingsqlite.DataBase.Constants.ID
import com.example.contactappusingsqlite.DataBase.Constants.NAME
import com.example.contactappusingsqlite.DataBase.Constants.PHONE_NUMBER
import com.example.contactappusingsqlite.DataBase.Constants.TABLE_NAME
import com.example.contactappusingsqlite.models.ContactData

class DBHelper(context: Context) :
    SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION), DBInterface {
    override fun onCreate(db: SQLiteDatabase?) {
        val query =
            "create table $TABLE_NAME ($ID integer not null primary key autoincrement unique, $NAME text not null, $PHONE_NUMBER text not null)"
        db?.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {}


    override fun addContact(contactData: ContactData) {
        val writableDatabase = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(ID, contactData.id)
        contentValues.put(NAME, contactData.contact_name)
        contentValues.put(PHONE_NUMBER, contactData.contact_number)
        writableDatabase.insert(TABLE_NAME, null, contentValues)
        writableDatabase.close()
    }


    override fun getContacts(): List<ContactData> {
        val list = ArrayList<ContactData>()
        val readableDatabase = this.readableDatabase
        val query = "select * from $TABLE_NAME"
        val cursor = readableDatabase.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(0)
                val name = cursor.getString(1)
                val number = cursor.getString(2)
                val contact = ContactData(id, name, number)
                list.add(contact)
            } while (cursor.moveToNext())
        }
        return list
    }

    override fun deleteContact(contactData: ContactData) {
        val database = this.writableDatabase
        database.delete(TABLE_NAME, "$ID = ?", arrayOf("${contactData.id}"))
        database.close()
    }

    override fun updateContact(contactData: ContactData): Int {
        val database = this.writableDatabase
        val contentValue = ContentValues()
        contentValue.put(ID, contactData.id)
        contentValue.put(NAME, contactData.contact_name)
        contentValue.put(PHONE_NUMBER, contactData.contact_number)

        return database.update(TABLE_NAME,
            contentValue,
            "$ID = ?",
            arrayOf(contactData.id.toString()))
    }


//    override fun getUsers(): List<ContactData> {
//        val list = ArrayList<ContactData>()
//        val query = "select * from $TABLE_NAME"
//        val readableDatabase = this.readableDatabase
//        val cursor = readableDatabase.rawQuery(query, null)
//
//        if (cursor.moveToFirst()) {
//            do {
//                val id = cursor.getInt(0)
//                val name = cursor.getString(1)
//                val phone = cursor.getString(2)
//                val contact = ContactData(id, name, phone)
//                list.add(contact)
//            } while (cursor.moveToNext())
//        }
//
//        return list
//    }
//
//    override fun deleteUser(contactData: ContactData) {
//        val database = this.writableDatabase
//        database.delete(TABLE_NAME, "$ID = ?", arrayOf(contactData.id.toString()))
//        database.close()
//    }
//
//    override fun updateContact(contactData: ContactData): Int {
//        val database = this.writableDatabase
//        val contentValue = ContentValues()
//        contentValue.put(ID, contactData.id)
//        contentValue.put(NAME, contactData.contact_name)
//        contentValue.put(PHONE_NUMBER, contactData.contact_number)
//
//        return database.update(TABLE_NAME,
//            contentValue,
//            "$ID = ?",
//            arrayOf(contactData.id.toString()))
//}

    fun searchContact(queryText: String): ArrayList<ContactData> {
        val readableDatabase = this.readableDatabase
        val list = ArrayList<ContactData>()
        val query = "select * from $TABLE_NAME where name like '%$queryText%'"
        val cursor = readableDatabase.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                val contact = ContactData(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2)
                )

                list.add(contact)
            } while (cursor.moveToNext())
        }
        return list
    }
}