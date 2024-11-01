package com.crissnm.registrousuarios.Componentes.Inicio

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BotonDeAlertaViewModel(context: Context) : ViewModel() {

    private val preferences: SharedPreferences =
        context.getSharedPreferences("button_states", Context.MODE_PRIVATE)

    private val _buttonStates = MutableStateFlow<Map<String, Boolean>>(loadButtonStates())
    val buttonStates = _buttonStates.asStateFlow()

    private val activeTimers = mutableMapOf<String, Boolean>()

    fun disableButton(buttonId: String, duration: Long = 30000L) {
        if (activeTimers[buttonId] == true) return

        viewModelScope.launch {
            activeTimers[buttonId] = true
            updateButtonState(buttonId, false)

            delay(duration)

            updateButtonState(buttonId, true)
            activeTimers.remove(buttonId)
        }
    }


    public suspend fun updateButtonState(buttonId: String, isActive: Boolean) {
        withContext(Dispatchers.Main) {
            val currentState = _buttonStates.value
            val newState = currentState.toMutableMap().apply { this[buttonId] = isActive }
            _buttonStates.emit(newState)
            saveButtonStates(newState)
        }
    }


    private fun loadButtonStates(): Map<String, Boolean> {
        val buttonStates = mutableMapOf<String, Boolean>()
        val buttonIds = listOf("alertButton_0", "alertButton_1", "alertButton_2", "alertButton_3", "alertButton_4")
        for (buttonId in buttonIds) {
            buttonStates[buttonId] = preferences.getBoolean(buttonId, true)
        }
        return buttonStates
    }

    private fun saveButtonStates(states: Map<String, Boolean>) {
        val editor = preferences.edit()
        states.forEach { (key, value) ->
            editor.putBoolean(key, value)
        }
        editor.apply()
    }
}
