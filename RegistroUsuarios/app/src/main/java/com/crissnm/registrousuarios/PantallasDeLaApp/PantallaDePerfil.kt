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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.crissnm.registrousuarios.ManejoDeUsuarios.User
import com.crissnm.registrousuarios.ManejoDeUsuarios.UserAuthService
import com.crissnm.registrousuarios.ManejoDeUsuarios.UserFireStoreService
import com.crissnm.registrousuarios.ManejoDeUsuarios.Validaciones
import com.crissnm.registrousuarios.Navegacion.ManejoDeLasPantallasDeLaApp

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PantallaDePerfil(navController: NavController, uid: String, authService: UserAuthService) {
    contenidoPantallaDePerfil(navController, uid, authService)
}

@Composable
fun contenidoPantallaDePerfil(navController: NavController, uid: String, authService: UserAuthService) {
    val context = LocalContext.current
    val firestoreService = UserFireStoreService()

    // Estado para almacenar el objeto User
    var user by remember { mutableStateOf<User?>(null) }

    // Estado para activar el modo de edición
    var isEditing by remember { mutableStateOf(false) }

    // Usar LaunchedEffect para recuperar el usuario basado en el UID
    LaunchedEffect(uid) {
        firestoreService.getUserbyUid(uid) { fetchedUser ->
            if (fetchedUser != null) {
                user = fetchedUser
            } else {
                Toast.makeText(context, "Usuario no encontrado", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Si el usuario ya fue cargado, asignar sus datos a variables
    var nombres by remember { mutableStateOf(user?.name ?: "") }
    var apellidos by remember { mutableStateOf(user?.lastname ?: "") }
    var telefono by remember { mutableStateOf(user?.number ?: "") }
    var correo by remember { mutableStateOf(user?.email ?: "") }
    var contrasena by remember { mutableStateOf(user?.password ?: "") }
    var cui by remember { mutableStateOf(user?.cui ?: "") }
    var departamento by remember { mutableStateOf(user?.department ?: "") }
    var municipio by remember { mutableStateOf(user?.municipality ?: "") }

    LaunchedEffect(user) {
        user?.let {
            nombres = it.name
            apellidos = it.lastname
            telefono = it.number
            correo = it.email
            contrasena = it.password
            cui = it.cui
            departamento = it.department
            municipio = it.municipality
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 40.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Perfil", fontSize = 24.sp, modifier = Modifier.padding(bottom = 12.dp),
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Serif)

        // Campos editables
        OutlinedTextField(
            value = nombres,
            onValueChange = { nombres = it },
            label = { Text("Nombres") },
            readOnly = !isEditing,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = apellidos,
            onValueChange = { apellidos = it },
            label = { Text("Apellidos") },
            readOnly = !isEditing,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = telefono,
            onValueChange = { telefono = it },
            label = { Text("Teléfono") },
            readOnly = !isEditing,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = correo,
            onValueChange = { correo = it },
            label = { Text("Correo") },
            readOnly = true,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = contrasena,
            onValueChange = { contrasena = it },
            label = { Text("Contraseña") },
            readOnly = true,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = cui,
            onValueChange = { cui = it },
            label = { Text("CUI") },
            readOnly = !isEditing,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = departamento,
            onValueChange = { departamento = it },
            label = { Text("Departamento") },
            readOnly = !isEditing,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = municipio,
            onValueChange = { municipio = it },
            label = { Text("Municipio") },
            readOnly = !isEditing,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween // Espaciado entre los botones
        ) {
            // Botón para alternar entre modo de edición y visualización, y guardar cambios
            Button(onClick = {
                if (isEditing) {
                    // Guardar los cambios
                    user?.let {
                        it.name = nombres
                        it.lastname = apellidos
                        it.number = telefono
                        it.email = correo
                        it.password = contrasena
                        it.cui = cui
                        it.department = departamento
                        it.municipality = municipio

                        firestoreService.updateUserInFirestore(it) { success ->
                            if (success) {
                                Toast.makeText(context, "Perfil actualizado con éxito", Toast.LENGTH_SHORT).show()
                                isEditing = false // Salir del modo de edición
                            } else {
                                Toast.makeText(context, "Error al actualizar perfil", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                } else {
                    // Cambiar al modo de edición
                    isEditing = true
                }
            }) {
                Text(text = if (isEditing) "Guardar Cambios" else "Editar Perfil")
            }

            // Botón para cerrar sesión
            Button(
                onClick = {
                    authService.logoutUser {
                        navController.navigate(ManejoDeLasPantallasDeLaApp.PantallaConInfoApp.ruta)
                        Toast.makeText(context, "Sesión cerrada", Toast.LENGTH_SHORT).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text(text = "Cerrar Sesión", color = Color.White)
            }
        }
    }
}
