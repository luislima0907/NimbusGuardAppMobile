package com.crissnm.registrousuarios.ManejoDeUsuarios

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class UserAuthService {
    private val auth:FirebaseAuth = Firebase.auth
    private val  serviceFireStore: UserFireStoreService = UserFireStoreService()


    fun registerUser(context: Context, user: User, callback: (String?) -> Unit) {
        Log.d("usuario data", user.toString())

        auth.createUserWithEmailAndPassword(user.email, user.password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userFirebase = auth.currentUser
                    if (userFirebase != null) {
                        user.email = userFirebase.email ?: ""
                        user.uid = userFirebase.uid
                        user.rol = "usuario"

                        serviceFireStore.saveUserInFireStore(user)


                        callback(user.uid)
                    } else {
                        Log.e("Registro", "No se pudo obtener el usuario actual.")
                        callback(null)
                    }
                } else {
                    Log.e("Registro", "Error al registrar: ${task.exception?.message}")
                    callback(null)
                }
            }
    }
    fun loginUser(context: Context, email: String, password: String, callback: (Boolean, String?) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userFirebase = auth.currentUser
                    if (userFirebase != null) {
                        Log.d("Login", "Inicio de sesión exitoso. UID: ${userFirebase.uid}")
                        callback(true, userFirebase.uid)
                    } else {
                        Log.e("Login", "Error: Usuario no encontrado tras inicio de sesión.")
                        callback(false, "Usuario no encontrado.")
                    }
                } else {
                    val errorMessage = task.exception?.message ?: "Error desconocido"
                    Log.e("Login", "Error al iniciar sesión: $errorMessage")
                    callback(false, errorMessage)
                }
            }
    }


}