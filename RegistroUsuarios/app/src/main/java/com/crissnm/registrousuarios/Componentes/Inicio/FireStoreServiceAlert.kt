package com.crissnm.registrousuarios.Componentes.Inicio

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FireStoreServiceAlert {
    private val firebaseStoreAlertRef = Firebase.firestore.collection("alerts")


   public fun saveAlertInFireStore(alert: Alert) {
       firebaseStoreAlertRef.add(alert)
   }
}