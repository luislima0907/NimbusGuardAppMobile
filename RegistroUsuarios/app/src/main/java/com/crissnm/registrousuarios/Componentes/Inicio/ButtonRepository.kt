package com.crissnm.registrousuarios.Componentes.Inicio

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ButtonRepository(private val context: Context) {
    private val _buttonStates = MutableLiveData<MutableMap<String, Boolean>>()
    val buttonStates: LiveData<MutableMap<String, Boolean>> get() = _buttonStates

    // Job para controlar el coroutine
    private var job: Job? = null

    init {
        _buttonStates.value = loadButtonStates()
    }

    fun disableButton(buttonId: String) {
        updateButtonState(buttonId, false)
        startButtonReactivationTimer(buttonId, 30_000) // 30 segundos de desactivación
    }

    fun enableButton(buttonId: String) {
        updateButtonState(buttonId, true)
    }

    private fun updateButtonState(buttonId: String, isEnabled: Boolean) {
        val currentStates = _buttonStates.value ?: mutableMapOf()
        currentStates[buttonId] = isEnabled
        _buttonStates.value = currentStates
        saveButtonState(buttonId, isEnabled)
    }

    private fun saveButtonState(buttonId: String, isEnabled: Boolean) {
        val sharedPreferences = context.getSharedPreferences("ButtonPrefs", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putBoolean(buttonId, isEnabled)
            apply()
        }
    }

    private fun loadButtonStates(): MutableMap<String, Boolean> {
        val sharedPreferences = context.getSharedPreferences("ButtonPrefs", Context.MODE_PRIVATE)
        val buttonStates = mutableMapOf<String, Boolean>()
        listOf("alertButton_0", "alertButton_1", "alertButton_2", "alertButton_3", "alertButton_4").forEach { buttonId ->
            buttonStates[buttonId] = sharedPreferences.getBoolean(buttonId, true) // True por defecto
        }
        return buttonStates
    }

    // Función para iniciar el temporizador
    private fun startButtonReactivationTimer(buttonId: String, delayMillis: Long) {
        job?.cancel() // Cancelar el trabajo anterior si existe
        job = CoroutineScope(Dispatchers.Main).launch {
            delay(delayMillis) // Esperar el tiempo especificado
            enableButton(buttonId) // Habilitar el botón
        }
    }

    fun clear() {
        job?.cancel() // Cancelar cualquier trabajo activo cuando ya no se necesite
    }
}
