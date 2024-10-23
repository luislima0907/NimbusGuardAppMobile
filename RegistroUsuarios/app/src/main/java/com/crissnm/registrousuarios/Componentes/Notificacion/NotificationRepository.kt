package com.crissnm.registrousuarios.Componentes.Notificacion

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf

object NotificacionRepository {
    // Lista observable de notificaciones
    val notificaciones = mutableStateListOf<Notificacion>()

    // Contador observable de notificaciones
    var contadorDeNotificaciones = mutableStateOf(0)
        private set

    // Función para agregar una nueva notificación
    fun agregarNotificacion(nuevaNotificacion: Notificacion) {
        notificaciones.add(nuevaNotificacion)
        contadorDeNotificaciones.value += 1
    }

}