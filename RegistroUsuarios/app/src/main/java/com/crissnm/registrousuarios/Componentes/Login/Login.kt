package com.crissnm.registrousuarios.Componentes.Login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.crissnm.registrousuarios.R

@Composable
fun Login(){
    val correo = remember { mutableStateOf("") }
    val contrasena = remember { mutableStateOf("") }

    ImagenLogin()
    CorreoField(correo)
    ContrasenaField(contrasena)
    ButtonLogin()

}
@Composable
fun ImagenLogin(){
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.login),
            contentDescription = "login",
            modifier = Modifier
                .size(50.dp)
                .offset(y = (-10).dp)

        )
    }
}

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

@Composable
fun ButtonLogin(){
    Button(
        onClick = { /* Acción del botón de inicio de sesión */ },
        modifier = Modifier.fillMaxWidth(
        )
    ) {
        Text(
            text = "Iniciar Sesión",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Serif
        )
    }
}


