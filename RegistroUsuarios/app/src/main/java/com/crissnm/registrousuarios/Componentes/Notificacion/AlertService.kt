package com.crissnm.registrousuarios.Componentes.Notificacion

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.crissnm.registrousuarios.Componentes.Inicio.Alert
import com.crissnm.registrousuarios.R
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AlertService(
    private val context: Context,
) {
    fun createNotification(
        alertId: String,
        estado: String,
        message: String,
        typeAlert: String,
        latitud: String,
        longitud: String
    ) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "alert_notifications"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Alert Notifications", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.logoappnimbusguard) // Cambia por el icono que desees
            .setContentTitle("Estado de Alerta: $estado")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        notificationManager.notify(alertId.hashCode(), notification.build())

        // Almacenar la notificación en el repositorio
        NotificacionRepository.agregarNotificacion(Notificacion(alertId, estado, message, typeAlert, latitud, longitud))
    }

    fun listenChangesOnAlerts(){
        val firebaseStoreAlertRef = Firebase.firestore.collection("alerts")
        firebaseStoreAlertRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w("FiresStore", "listen:error", e)
                return@addSnapshotListener
            }
            if (snapshot != null && !snapshot.isEmpty) {
                for(document in snapshot.documentChanges){
                    if(document.type == DocumentChange.Type.MODIFIED){
                        val alert = document.document.toObject(Alert::class.java)
                        createNotification(
                            alertId =  alert.idAlert,
                            estado = alert.state,
                            message =  alert.detail,
                            typeAlert =  alert.typeAlert,
                            latitud =  alert.latitude.toString(),
                            longitud =  alert.longitude.toString()
                        )
                    }
                }
            }
        }
    }
}