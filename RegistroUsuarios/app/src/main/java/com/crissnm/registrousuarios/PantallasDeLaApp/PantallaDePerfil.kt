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

    // Estado para mostrar el diálogo de edición de perfil
    var showEditDialog by remember { mutableStateOf(false) }

    // Estado para el diálogo de confirmación de eliminación
    var showDeleteConfirmation by remember { mutableStateOf(false) }

    // Usar LaunchedEffect para recuperar el usuario basado en el UID
    LaunchedEffect(uid) {
        firestoreService.getUserbyUid(uid) { fetchedUser ->
            if (fetchedUser != null) {
                user = fetchedUser
            } else {
                // Mostramos un mensaje de error si el usuario no es encontrado
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

    // Actualizar los valores de los campos editables cuando se carga el usuario
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

    // Estado del contador para eliminar cuenta
    var isDeleteButtonEnabled by remember { mutableStateOf(false) }
    var timerText by remember { mutableStateOf("Eliminar Cuenta (10)") } // Texto inicial del botón

    // Temporizador de cuenta regresiva de 10 segundos
    LaunchedEffect(showDeleteConfirmation) {
        if (showDeleteConfirmation) {
            object : CountDownTimer(10000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    val secondsLeft = millisUntilFinished / 1000
                    timerText = "Eliminar Cuenta ($secondsLeft)"
                }

                override fun onFinish() {
                    isDeleteButtonEnabled = true
                    timerText = "Eliminar Cuenta"
                }
            }.start()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Perfil", fontSize = 24.sp, modifier = Modifier.padding(bottom = 16.dp))

        // Mostrar la información del usuario recuperada
        Text(text = "Nombres: $nombres")
        Text(text = "Apellidos: $apellidos")
        Text(text = "Teléfono: $telefono")
        Text(text = "Correo: $correo")
        //Text(text = "Contraseña: $contrasena")
        Text(text = "CUI: $cui")
        Text(text = "Departamento: $departamento")
        Text(text = "Municipio: $municipio")

        Spacer(modifier = Modifier.height(20.dp))

        // Botón para abrir el diálogo de edición de perfil
        Button(onClick = {
            showEditDialog = true // Mostrar el diálogo para editar el perfil
        }) {
            Text(text = "Editar Perfil")
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Botón para eliminar la cuenta del usuario
        Button(
            onClick = {
                showDeleteConfirmation = true // Mostrar el diálogo de confirmación
                isDeleteButtonEnabled = false // Deshabilitar el botón de eliminación
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
        ) {
            Text(text = "Eliminar Cuenta", color = Color.White)
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Botón para cerrar sesión
        Button(
            onClick = {
                authService.logoutUser {
                    navController.navigate(ManejoDeLasPantallasDeLaApp.PantallaConInfoApp.ruta)
                    Toast.makeText(context, "Sesión cerrada", Toast.LENGTH_SHORT).show()
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
        ) {
            Text(text = "Cerrar Sesión", color = Color.White)
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
                    Text("¿Estás seguro de que deseas eliminar tu cuenta? Esta acción no se puede deshacer.")
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (isDeleteButtonEnabled) {
                                firestoreService.deleteUserFromFirestore(user!!) {
                                    if (it) {
                                        Toast.makeText(context, "Cuenta eliminada con éxito", Toast.LENGTH_SHORT).show()
                                        navController.popBackStack()
                                    } else {
                                        Toast.makeText(context, "Error al eliminar la cuenta", Toast.LENGTH_SHORT).show()
                                    }
                                }
                                showDeleteConfirmation = false
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                        enabled = isDeleteButtonEnabled
                    ) {
                        Text(text = timerText, color = Color.White) // Mostrar el texto actualizado
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

        // Diálogo de edición de perfil
        if (showEditDialog) {
            AlertDialog(
                onDismissRequest = {
                    showEditDialog = false // Cerrar el diálogo si se cancela
                },
                title = {
                    Text(text = "Editar Perfil")
                },
                text = {
                    // Campos editables dentro del diálogo
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
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

                        OutlinedTextField(
                            value = cui,
                            onValueChange = { cui = it },
                            label = { Text("CUI") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = departamento,
                            onValueChange = { departamento = it },
                            label = { Text("Departamento") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = municipio,
                            onValueChange = { municipio = it },
                            label = { Text("Municipio") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            // Validaciones antes de actualizar
                            when {
                                nombres.isBlank() || apellidos.isBlank() || telefono.isBlank() || correo.isBlank() || contrasena.isBlank() || cui.isBlank() || departamento.isBlank() || municipio.isBlank() -> {
                                    Toast.makeText(context, "Todos los campos deben estar completos", Toast.LENGTH_SHORT).show()
                                }
                                !Validaciones.isValidCorreo(correo) -> {
                                    Toast.makeText(context, "Correo inválido", Toast.LENGTH_SHORT).show()
                                }
                                else -> {
                                    // Actualización directa en el objeto user
                                    user!!.name = nombres
                                    user!!.lastname = apellidos
                                    user!!.number = telefono
                                    user!!.email = correo
                                    user!!.password = contrasena
                                    user!!.cui = cui
                                    user!!.department = departamento
                                    user!!.municipality = municipio

                                    // Actualización en Firestore
                                    firestoreService.updateUserInFirestore(user!!) {
                                        if (it) {
                                            Toast.makeText(context, "Perfil actualizado con éxito", Toast.LENGTH_SHORT).show()
                                            showEditDialog = false // Cierra el diálogo después de la actualización
                                        } else {
                                            Toast.makeText(context, "Error al actualizar perfil", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Green)
                    ) {
                        Text(text = "Guardar Cambios", color = Color.White)
                    }
                },
                dismissButton = {
                    Button(onClick = {
                        showEditDialog = false // Cerrar el diálogo sin cambios
                    }) {
                        Text(text = "Cancelar")
                    }
                }
            )
        }
    }
}




