package com.crissnm.registrousuarios

data class User (
    val nombres: String,
    val apellidos: String,
    val cui: String,
    val telefono: String,
    val departamento: String,
    val municipio: String,
    val correo: String,
    val contrasena: String
)
