package com.crissnm.registrousuarios.Componentes.Login

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation

@Composable
fun CorreoField(correo: MutableState<String>) {
    OutlinedTextField(
        value = correo.value,
        onValueChange = { correo.value = it },
        label = { Text("Correo") },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
        singleLine = true
    )
}

@Composable
fun ContrasenaField(contrasena: MutableState<String>) {
    OutlinedTextField(
        value = contrasena.value,
        onValueChange = { contrasena.value = it },
        label = { Text("Contraseña") },
        modifier = Modifier.fillMaxWidth(),
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
        singleLine = true
    )
}
