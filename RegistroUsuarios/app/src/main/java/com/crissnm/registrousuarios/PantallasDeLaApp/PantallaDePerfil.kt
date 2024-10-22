package com.crissnm.registrousuarios.PantallasDeLaApp

import android.annotation.SuppressLint
import android.os.CountDownTimer
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.crissnm.registrousuarios.ManejoDeUsuarios.User
import com.crissnm.registrousuarios.ManejoDeUsuarios.Validaciones
import com.crissnm.registrousuarios.Navegacion.ManejoDeLasPantallasDeLaApp

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PantallaDePerfil(navController: NavController, user: User) {
    contenidoPantallaDePerfil(navController, user)
}

@Composable
fun contenidoPantallaDePerfil(navController: NavController, user: User) {
    val context = LocalContext.current

    // Campos editables con la información del usuario
    var nombres by remember { mutableStateOf(user.name) }
    var apellidos by remember { mutableStateOf(user.lastname) }
    var telefono by remember { mutableStateOf(user.number) }
    var correo by remember { mutableStateOf(user.email) }
    var contrasena by remember { mutableStateOf(user.password) }

    // Estados para el temporizador y control de eliminación
    var showDeleteConfirmation by remember { mutableStateOf(false) } // Muestra el diálogo de confirmación
    var enableFinalDeleteButton by remember { mutableStateOf(false) } // Habilita el botón después de los 10 segundos
    var timerText by remember { mutableStateOf("10") }

    // Función para iniciar el temporizador de 10 segundos
    fun startDeleteCountdown() {
        enableFinalDeleteButton = false // El botón se deshabilita al comenzar el temporizador
        timerText = "10" // Reinicia el temporizador
        object : CountDownTimer(10000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timerText = (millisUntilFinished / 1000).toString() // Actualiza el temporizador cada segundo
            }

            override fun onFinish() {
                enableFinalDeleteButton = true // Habilita el botón para la eliminación definitiva
                timerText = "Activado"
            }
        }.start()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Editar Perfil", fontSize = 24.sp, modifier = Modifier.padding(bottom = 16.dp))

        // Campos editables
        OutlinedTextField(
            value = nombres,
            onValueChange = { nombres = it },
            label = { Text("Nombres") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = apellidos,
            onValueChange = { apellidos = it },
            label = { Text("Apellidos") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = telefono,
            onValueChange = { telefono = it },
            label = { Text("Teléfono") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone),
            singleLine = true
        )

        OutlinedTextField(
            value = correo,
            onValueChange = { correo = it },
            label = { Text("Correo") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
            singleLine = true
        )

        OutlinedTextField(
            value = contrasena,
            onValueChange = { contrasena = it },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Botón para actualizar la información (simulación sin base de datos)
        Button(onClick = {
            // Validaciones de los campos antes de actualizar
            when {
                nombres.isBlank() || apellidos.isBlank() || telefono.isBlank() || correo.isBlank() || contrasena.isBlank() -> {
                    Toast.makeText(context, "Todos los campos deben estar completos", Toast.LENGTH_SHORT).show()
                }
                !Validaciones.isValidCorreo(correo) -> {
                    Toast.makeText(context, "Correo inválido", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    // Aquí simplemente simulas la actualización sin conexión a base de datos
                    Toast.makeText(context, "Perfil actualizado (simulado)", Toast.LENGTH_SHORT).show()
                }
            }
        }) {
            Text(text = "Actualizar Perfil")
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Botón para eliminar la cuenta del usuario
        Button(
            onClick = {
                showDeleteConfirmation = true // Activa el diálogo de confirmación
                startDeleteCountdown() // Inicia el temporizador
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
        ) {
            Text(text = "Eliminar Cuenta", color = Color.White)
        }

        // Diálogo de confirmación de eliminación
        if (showDeleteConfirmation) {
            AlertDialog(
                onDismissRequest = {
                    showDeleteConfirmation = false // Cierra el diálogo si se cancela
                },
                title = {
                    Text(text = "Confirmar eliminación de cuenta")
                },
                text = {
                    Text("Estás a punto de eliminar tu cuenta. ¿Estás seguro? Espera $timerText segundos para poder confirmar.")
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (enableFinalDeleteButton) {
                                // Simulación de eliminación de cuenta sin base de datos
                                navController.popBackStack()
                                Toast.makeText(context, "Cuenta eliminada (simulado)", Toast.LENGTH_SHORT).show()
                                showDeleteConfirmation = false // Cierra el diálogo después de eliminar
                            }
                        },
                        enabled = enableFinalDeleteButton, // Habilitar el botón solo cuando el temporizador finalice
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                    ) {
                        Text(text = "Eliminar Cuenta ($timerText)", color = Color.White)
                    }
                },
                dismissButton = {
                    Button(onClick = {
                        showDeleteConfirmation = false // Cerrar el diálogo si el usuario se arrepiente
                        Toast.makeText(context, "Eliminación cancelada", Toast.LENGTH_SHORT).show()
                    }) {
                        Text(text = "Cancelar")
                    }
                }
            )
        }
    }
}


