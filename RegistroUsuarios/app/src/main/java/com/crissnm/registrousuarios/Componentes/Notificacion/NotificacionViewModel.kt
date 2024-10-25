package com.crissnm.registrousuarios.Componentes.Notificacion

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class NotificacionViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    // Lista de notificaciones que se mantiene en memoria mientras la app esté en ejecución
    private val _notificaciones = savedStateHandle.getLiveData<List<Notificacion>>("notificaciones", emptyList())
    val notificaciones = _notificaciones

    // Función para agregar una nueva notificación
    fun agregarNotificacion(notificacion: Notificacion) {
        val notificacionesActuales = _notificaciones.value.orEmpty().toMutableList()
        notificacionesActuales.add(notificacion)
        _notificaciones.value = notificacionesActuales
    }

    fun agregarNuevaNotificacion(viewModel: NotificacionViewModel, notificacion: Notificacion) {
        viewModel.agregarNotificacion(notificacion)
    }

    // Función para establecer una lista de notificaciones
    fun establecerNotificaciones(nuevasNotificaciones: List<Notificacion>) {
        _notificaciones.value = nuevasNotificaciones
    }
}
