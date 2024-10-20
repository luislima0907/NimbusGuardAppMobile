package com.crissnm.registrousuarios.ManejoDeUsuarios

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class UserFireStoreService {
    private val firebaseStoreUserRef = Firebase.firestore.collection("users")

    fun saveUserInFireStore(user: User) {
        firebaseStoreUserRef.document(user.uid).set(user)
    }

    fun getUserbyUid(uid: String, callback: (User?) -> Unit) {
        Log.d("DEBUG", "UID recibido: $uid")

        firebaseStoreUserRef.document(uid).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val user = document.toObject(User::class.java)
                    callback(user)
                    Log.d("DEBUG", "Usuario recuperado: $user")

                } else {
                    Log.d("DEBUG", "No se encontró el usuario con UID: $uid")

                    callback(null)
                }
            }
            .addOnFailureListener { exception ->
                exception.printStackTrace()
                callback(null)
            }
    }
}


