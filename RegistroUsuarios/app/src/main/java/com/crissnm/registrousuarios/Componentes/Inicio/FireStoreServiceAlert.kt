package com.crissnm.registrousuarios.Componentes.Inicio

import com.google.firebase.firestore.FirebaseFirestore

class FireStoreServiceAlert {
    fun saveAlertInFireStore(alert: Alert, onSuccess: (String) -> Unit, onFailure: () -> Unit) {
        val db = FirebaseFirestore.getInstance()

        // Agregar la alerta a Firestore y obtener su ID
        db.collection("alerts")
            .add(alert) // Aquí se guarda la alerta
            .addOnSuccessListener { documentReference ->
                val alertId = documentReference.id // Obtener el ID generado por Firestore
                onSuccess(alertId) // Devolver el ID a través del callback onSuccess
            }
            .addOnFailureListener {
                onFailure() // Llamar al callback de fallo
            }
    }
}

