package com.crissnm.registrousuarios.Componentes.Notificacion

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.crissnm.registrousuarios.R

class AlertService(private val context: Context) {

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
}
