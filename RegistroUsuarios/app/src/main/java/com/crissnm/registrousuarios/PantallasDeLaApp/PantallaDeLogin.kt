package com.crissnm.registrousuarios.PantallasDeLaApp

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.crissnm.registrousuarios.Componentes.Login.ContrasenaField
import com.crissnm.registrousuarios.Componentes.Login.CorreoField
import com.crissnm.registrousuarios.ManejoDeUsuarios.User
import com.crissnm.registrousuarios.ManejoDeUsuarios.Validaciones
import com.crissnm.registrousuarios.Navegacion.ManejoDeLasPantallasDeLaApp

@Composable
fun PantallaDeLogin(navController: NavController, users: List<User>) {
    LoginForm(navController, users)
}

@Composable
fun LoginForm(navController: NavController, users: List<User>) {
    // Estados para los campos de correo y contraseña
    val correo = remember { mutableStateOf("") }
    val contrasena = remember { mutableStateOf("") }
    val context = LocalContext.current

    // Estado para controlar el diálogo de error o éxito
    val showLoginSuccessDialog = remember { mutableStateOf(false) }
    val showLoginErrorDialog = remember { mutableStateOf(false) }

    val scrollState = rememberScrollState() // Estado para el scroll

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 20.dp, end = 20.dp, top = 90.dp, bottom = 10.dp)
            .verticalScroll(scrollState), // Habilitar desplazamiento
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Iniciar Sesión",
            fontWeight = FontWeight.Bold,
            fontSize = 27.sp,
            color = Color.Black,
            fontFamily = FontFamily.Serif
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campos de texto para correo y contraseña
        CorreoField(correo)
        ContrasenaField(contrasena)

        Spacer(modifier = Modifier.height(3.dp))

        Button(
            onClick = {
                // Verificar si los campos están vacíos
                if (correo.value.isBlank()) {
                    Toast.makeText(context, "El campo de correo está vacío", Toast.LENGTH_SHORT).show()
                } else if (contrasena.value.isBlank()) {
                    Toast.makeText(context, "El campo de contraseña está vacío", Toast.LENGTH_SHORT).show()
                } else {
                    // Validar formato del correo
                    if (!Validaciones.isValidCorreo(correo.value)) {
                        Toast.makeText(context, "Correo inválido", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    // Buscar en la lista de usuarios registrados
                    val user = users.find { it.correo == correo.value }

                    // Si el usuario existe y la contraseña coincide
                    if (user != null && user.contrasena == contrasena.value) {
                        // Mostrar diálogo de éxito si las credenciales coinciden
                        showLoginSuccessDialog.value = true
                    } else {
                        // Mostrar diálogo de error si las credenciales no coinciden
                        showLoginErrorDialog.value = true
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Blue,  // Consistencia en el color del botón
                contentColor = Color.White    // Texto blanco
            )
        ) {
            Text(
                text = "Iniciar Sesión",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                fontFamily = FontFamily.Serif
            )
        }
    }

    // Diálogo de inicio de sesión exitoso
    if (showLoginSuccessDialog.value) {
        AlertDialog(
            onDismissRequest = { showLoginSuccessDialog.value = false },
            title = { Text(text = "Inicio de Sesión Exitoso!", fontWeight = FontWeight.Bold, fontSize = 20.sp) },
            text = { Text(text = "¡Bienvenido de nuevo!") },
            confirmButton = {
                Button(
                    onClick = {
                        showLoginSuccessDialog.value = false
                        navController.navigate(ManejoDeLasPantallasDeLaApp.PantallaPrincipal.ruta)
                    }
                ) {
                    Text("Aceptar")
                }
            },
            properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
        )
    }

    // Diálogo de error de inicio de sesión
    if (showLoginErrorDialog.value) {
        AlertDialog(
            onDismissRequest = { showLoginErrorDialog.value = false },
            title = { Text(text = "Error", fontWeight = FontWeight.Bold, fontSize = 20.sp) },
            text = {
                Text(text = "Correo o contraseña incorrectos. Por favor, inténtalo de nuevo.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        showLoginErrorDialog.value = false
                    }
                ) {
                    Text("Aceptar")
                }
            },
            properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLoginForm() {
    // Estados vacíos solo para ver los campos
    val correo = remember { mutableStateOf("") }
    val contrasena = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 20.dp, end = 20.dp, top = 90.dp, bottom = 10.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Iniciar Sesión",
            fontWeight = FontWeight.Bold,
            fontSize = 27.sp,
            color = Color.Black,
            fontFamily = FontFamily.Serif
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campos de texto para correo y contraseña
        CorreoField(correo)
        ContrasenaField(contrasena)

        Spacer(modifier = Modifier.height(3.dp))

        Button(
            onClick = { /* Acción vacía para la vista previa */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Blue,  // Consistencia en el color del botón
                contentColor = Color.White    // Texto blanco
            )
        ) {
            Text(
                text = "Iniciar Sesión",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                fontFamily = FontFamily.Serif
            )
        }
    }
}



