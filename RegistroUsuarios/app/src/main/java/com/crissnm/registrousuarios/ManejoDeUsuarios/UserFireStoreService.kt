package com.crissnm.registrousuarios.ManejoDeUsuarios

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class UserFireStoreService {
    private val firebaseStoreUserRef = Firebase.firestore.collection("users")

    fun saveUserInFireStore(user: User) {
        firebaseStoreUserRef.document(user.uid).set(user)
    }

    fun getUserbyUid(uid: String, callback: (User?) -> Unit) {
        // Asegúrate de que uid no esté vacío y sea válido
        if (uid.isNotEmpty()) {
            val docRef = FirebaseFirestore.getInstance().collection("users").document(uid)
            docRef.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val user = document.toObject(User::class.java)
                        callback(user)
                    } else {
                        callback(null) // Documento no encontrado
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w("FirestoreError", "Error getting document: ", exception)
                    callback(null) // En caso de error
                }
        } else {
            callback(null) // UID vacío
        }
    }

//    fun getUserbyUid(uid: String, callback: (User?) -> Unit) {
//        Log.d("DEBUG", "UID recibido: $uid")
//        firebaseStoreUserRef.document(uid).get()
//            .addOnSuccessListener { document ->
//                if (document.exists()) {
//                    val user = document.toObject(User::class.java)
//                    callback(user)
//                    Log.d("DEBUG", "Usuario recuperado: $user")
//
//                } else {
//                    Log.d("DEBUG", "No se encontró el usuario con UID: $uid")
//
//                    callback(null)
//                }
//            }
//            .addOnFailureListener { exception ->
//                exception.printStackTrace()
//                callback(null)
//            }
//    }
    // Eliminar un usuario por UID
    fun deleteUserFromFirestore(user: User, callback: (Boolean) -> Unit) {
        firebaseStoreUserRef.document(user.uid).delete()
            .addOnSuccessListener {
                Log.d("DEBUG", "Usuario eliminado: ${user.uid}")
                callback(true)
            }
            .addOnFailureListener { exception ->
                Log.e("ERROR", "Error al eliminar el usuario: ${exception.message}")
                callback(false)
            }
    }

    // Actualizar un usuario en Firestore
    fun updateUserInFirestore(user: User, callback: (Boolean) -> Unit) {
        firebaseStoreUserRef.document(user.uid).set(user)
            .addOnSuccessListener {
                Log.d("DEBUG", "Usuario actualizado: ${user.uid}")
                callback(true)
            }
            .addOnFailureListener { exception ->
                Log.e("ERROR", "Error al actualizar el usuario: ${exception.message}")
                callback(false)
            }
    }
}


