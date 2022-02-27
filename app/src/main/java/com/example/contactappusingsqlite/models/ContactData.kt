package com.example.contactappusingsqlite.models

class ContactData {
    var id: Int? = null
    var contact_name: String? = null
    var contact_number: String? = null

    constructor()

    constructor(contact_name: String?) {
        this.contact_name = contact_name
    }

    constructor(contact_name: String?, contact_number: String?) {
        this.contact_name = contact_name
        this.contact_number = contact_number
    }

    constructor(id: Int?, contact_name: String?, contact_number: String) {
        this.id = id
        this.contact_name = contact_name
        this.contact_number = contact_number
    }
}
