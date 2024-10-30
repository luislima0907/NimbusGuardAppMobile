package com.crissnm.registrousuarios.Componentes.Inicio

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class BotonDeAlertaViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BotonDeAlertaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BotonDeAlertaViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

