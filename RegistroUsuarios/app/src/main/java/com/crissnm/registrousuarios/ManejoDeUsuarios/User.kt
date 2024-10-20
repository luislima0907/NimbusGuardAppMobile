package com.crissnm.registrousuarios.ManejoDeUsuarios


class User() {

    var uid: String = ""
    var name: String = ""
    var lastname: String = ""
    var email: String = ""
    var password: String = ""
    var cui: String = ""
    var number: String = ""
    var department: String = ""
    var municipality: String = ""
    var rol: String =""


    constructor(
        uid: String,
        name: String,
        lastname: String,
        email: String,
        password: String,
        cui: String,
        number: String,
        department: String,
        municipality: String
    ) : this() {
        this.uid = uid
        this.name = name
        this.lastname = lastname
        this.email = email
        this.password = password
        this.cui = cui
        this.number = number
        this.department = department
        this.municipality = municipality
    }

    override fun toString(): String {
        return "User(uid='$uid', name='$name', lastname='$lastname', email='$email', password='$password', cui='$cui', number='$number', department='$department', municipality='$municipality', rol='$rol')"
    }
}
