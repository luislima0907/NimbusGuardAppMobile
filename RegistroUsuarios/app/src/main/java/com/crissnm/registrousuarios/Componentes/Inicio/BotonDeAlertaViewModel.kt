package com.crissnm.registrousuarios.Componentes.Inicio

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class BotonDeAlertaViewModel(private val context: Context) : ViewModel() {
    private val buttonRepository = ButtonRepository(context)

    val buttonStates: LiveData<MutableMap<String, Boolean>> get() = buttonRepository.buttonStates

    fun disableButton(buttonId: String) {
        buttonRepository.disableButton(buttonId)
    }

    fun enableButton(buttonId: String) {
        buttonRepository.enableButton(buttonId)
    }

    override fun onCleared() {
        super.onCleared()
        buttonRepository.clear() // Limpiar cualquier coroutine activa
    }
}



//class BotonDeAlertaViewModel(private val context: Context) : ViewModel() {
//    // Variable para el estado de cada botón
//    private val _buttonStates = MutableLiveData<MutableMap<String, Boolean>>()
//    val buttonStates: LiveData<MutableMap<String, Boolean>> get() = _buttonStates
//
//    // Job para controlar el coroutine
//    private var job: Job? = null
//
//    init {
//        // Inicializar el estado de los botones desde SharedPreferences
//        _buttonStates.value = loadButtonStates(context)
//    }
//
//    // Función para deshabilitar un botón específico
//    fun disableButton(buttonId: String) {
//        updateButtonState(buttonId, false)
//        startButtonReactivationTimer(buttonId, 30_000) // 30 segundos de desactivación
//    }
//
//    // Función para habilitar un botón específico
//    fun enableButton(buttonId: String) {
//        updateButtonState(buttonId, true)
//    }
//
//    // Función para actualizar el estado de un botón
//    private fun updateButtonState(buttonId: String, isEnabled: Boolean) {
//        val currentStates = _buttonStates.value ?: mutableMapOf()
//        currentStates[buttonId] = isEnabled
//        _buttonStates.value = currentStates
//        saveButtonState(context, buttonId, isEnabled)
//    }
//
//    // Función para iniciar el temporizador
//    private fun startButtonReactivationTimer(buttonId: String, delayMillis: Long) {
//        job?.cancel() // Cancelar el trabajo anterior si existe
//        job = CoroutineScope(Dispatchers.Main).launch {
//            delay(delayMillis) // Esperar el tiempo especificado
//            enableButton(buttonId) // Habilitar el botón
//        }
//    }
//
//    // Funciones para manejar SharedPreferences
//    private fun saveButtonState(context: Context, buttonId: String, isEnabled: Boolean) {
//        val sharedPreferences = context.getSharedPreferences("ButtonPrefs", Context.MODE_PRIVATE)
//        with(sharedPreferences.edit()) {
//            putBoolean(buttonId, isEnabled)
//            apply()
//        }
//    }
//
//    private fun loadButtonStates(context: Context): MutableMap<String, Boolean> {
//        val sharedPreferences = context.getSharedPreferences("ButtonPrefs", Context.MODE_PRIVATE)
//        val buttonStates = mutableMapOf<String, Boolean>()
//        // Aquí puedes definir la lista de todos los botones que necesitas manejar
//        listOf("alertButton_0", "alertButton_1", "alertButton_2", "alertButton_3", "alertButton_4").forEach { buttonId ->
//            buttonStates[buttonId] = sharedPreferences.getBoolean(buttonId, true) // True por defecto
//        }
//        return buttonStates
//    }
//
//    override fun onCleared() {
//        super.onCleared()
//        job?.cancel() // Cancelar cualquier trabajo activo cuando el ViewModel se destruye
//    }
//}