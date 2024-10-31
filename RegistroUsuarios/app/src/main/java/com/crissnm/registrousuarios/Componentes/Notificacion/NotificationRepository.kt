package com.crissnm.registrousuarios.Componentes.Notificacion

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf

object NotificacionRepository {
    private val notificacionesPorUsuario = mutableMapOf<String, MutableList<Notificacion>>()

    private val notificacionesObservables = mutableStateMapOf<String, MutableList<Notificacion>>()

    private val contadoresDeNotificaciones = mutableStateMapOf<String, Int>()

    fun agregarNotificacion(uid: String, nuevaNotificacion: Notificacion) {
        val listaNotificaciones = notificacionesObservables.getOrPut(uid) { mutableStateListOf() }

        listaNotificaciones.add(nuevaNotificacion)

        contadoresDeNotificaciones[uid] = listaNotificaciones.size
    }

    fun obtenerNotificaciones(uid: String): List<Notificacion> {
        return notificacionesObservables[uid] ?: emptyList()
    }

    fun limpiarNotificaciones(uid: String) {
        notificacionesObservables.remove(uid)
        contadoresDeNotificaciones.remove(uid)
    }

    fun obtenerContadorDeNotificaciones(uid: String): Int {
        return contadoresDeNotificaciones[uid] ?: 0
    }
}
