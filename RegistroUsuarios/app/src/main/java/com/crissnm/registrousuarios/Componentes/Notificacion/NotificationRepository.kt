package com.crissnm.registrousuarios.Componentes.Notificacion

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf

object NotificacionRepository {
    // Mapa de notificaciones por usuario (UID)
    private val notificacionesPorUsuario = mutableMapOf<String, MutableList<Notificacion>>()

    // Estado observable para las notificaciones de cada usuario
    private val notificacionesObservables = mutableStateMapOf<String, MutableList<Notificacion>>()

    // Contador observable de notificaciones por usuario
    var contadorDeNotificaciones = mutableStateOf(0)
        private set

    // Función para agregar una nueva notificación para un usuario específico
    fun agregarNotificacion(uid: String, nuevaNotificacion: Notificacion) {
        // Obtener o crear la lista de notificaciones para el usuario
        val listaNotificaciones = notificacionesObservables.getOrPut(uid) { mutableStateListOf() }

        // Agregar la nueva notificación a la lista
        listaNotificaciones.add(nuevaNotificacion)

        // Actualizar el contador de notificaciones en tiempo real
        contadorDeNotificaciones.value = listaNotificaciones.size
    }

    // Función para obtener las notificaciones de un usuario específico
    fun obtenerNotificaciones(uid: String): List<Notificacion> {
        return notificacionesObservables[uid] ?: emptyList()
    }

    // Función para limpiar las notificaciones de un usuario al cerrar sesión
    fun limpiarNotificaciones(uid: String) {
        notificacionesObservables.remove(uid)
    }
}

