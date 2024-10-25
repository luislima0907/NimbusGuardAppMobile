package com.crissnm.registrousuarios.PantallasDeLaApp

import android.annotation.SuppressLint
import android.os.CountDownTimer
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.crissnm.registrousuarios.Componentes.pantallainicial.fontFamily
import com.crissnm.registrousuarios.DepYmuni.ValidarCUI
import com.crissnm.registrousuarios.ManejoDeUsuarios.UserAuthService
import com.crissnm.registrousuarios.ManejoDeUsuarios.UserFireStoreService
import com.crissnm.registrousuarios.ManejoDeUsuarios.UserProfileViewModel
import com.crissnm.registrousuarios.ManejoDeUsuarios.UserProfileViewModelFactory
import com.crissnm.registrousuarios.ManejoDeUsuarios.Validaciones
import com.crissnm.registrousuarios.Navegacion.ManejoDeLasPantallasDeLaApp
import com.crissnm.registrousuarios.R

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PantallaDePerfil(navController: NavController, uid: String, authService: UserAuthService) {
    // Obtener instancia del ViewModel
    val userProfileViewModel: UserProfileViewModel = viewModel(
        factory = UserProfileViewModelFactory(UserFireStoreService(), ValidarCUI())
    )

    // Cargar los datos del usuario si no se han cargado
    LaunchedEffect(uid) {
        userProfileViewModel.loadUser(uid)
    }

    contenidoPantallaDePerfil(
        navController =  navController,
        userProfileViewModel =  userProfileViewModel,
        uid =  uid,
        authService =  authService
    )
}

@Composable
fun contenidoPantallaDePerfil(
    navController: NavController,
    userProfileViewModel: UserProfileViewModel,
    uid: String,
    authService: UserAuthService,
) {
    val context = LocalContext.current
    val firestoreService = UserFireStoreService()
    val validarCUI = ValidarCUI() // Instancia de ValidarCUI
    var isDeleteButtonEnabled by remember { mutableStateOf(false) }
    var showDeleteConfirmation by remember { mutableStateOf(false) }
    var timerText by remember { mutableStateOf("Eliminar Cuenta (10)") }

    // Temporizador de cuenta regresiva para la eliminación de la cuenta
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

    // Estado para almacenar el objeto User
    val user by userProfileViewModel.user.observeAsState()

    // Estado para activar el modo de edición
    var isEditing by remember { mutableStateOf(false) }

    // Estados para los campos de texto del perfil
    var nombres by remember { mutableStateOf("") }
    var apellidos by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var cui by remember { mutableStateOf("") }
    var departamento by remember { mutableStateOf("") }
    var municipio by remember { mutableStateOf("") }

    // Actualizar los campos cuando se carga el usuario
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
            .verticalScroll(rememberScrollState())  // Habilitar scroll
            .padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 40.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(30.dp))

        Text("Perfil", fontSize = 28.sp, modifier = Modifier.padding(bottom = 12.dp),
            //fontWeight = FontWeight.Bold,
            fontFamily = fontFamily
        )

        // Campos de texto editables
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
            readOnly = true, // El correo no es editable
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = contrasena,
            onValueChange = { contrasena = it },
            label = { Text("Contraseña") },
            readOnly = true, // La contraseña no es editable desde esta pantalla
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = cui,
            onValueChange = { cui = it },
            label = { Text("CUI") },
            readOnly = !isEditing,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = departamento,
            onValueChange = { departamento = it },
            label = { Text("Departamento") },
            readOnly = true,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = municipio,
            onValueChange = { municipio = it },
            label = { Text("Municipio") },
            readOnly = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp) // Espacio entre los grupos de botones
        ) {
            // Primera fila con dos botones
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly // Espacio uniforme entre los botones
            ) {
                Button(onClick = {
                    if (isEditing) {
                        // para guardar los cambios
                        if (validarCampos(nombres, apellidos, telefono, cui, context)) {
                            // Validar CUI y obtener municipio y departamento
                            val (nuevoMunicipio, nuevoDepartamento) = validarCUI.obtenerMunicipioYDepartamento(cui)

                            if (nuevoMunicipio.isNotEmpty() && nuevoDepartamento.isNotEmpty()) {
                                // Actualizar los estados con los nuevos valores
                                municipio = nuevoMunicipio
                                departamento = nuevoDepartamento

                                // Actualizar la información del usuario
                                user?.let {
                                    it.name = nombres
                                    it.lastname = apellidos
                                    it.number = telefono
                                    it.cui = cui
                                    it.department = departamento // Usar el estado actualizado
                                    it.municipality = municipio // Usar el estado actualizado

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
                                Toast.makeText(context, "CUI inválido, actualice el campo", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        isEditing = true // Entrar en modo de edición
                    }
                }) {
                    Text(
                        text = if (isEditing) "Guardar Cambios" else "Editar Perfil",
                        color = Color.White,
                        fontFamily = fontFamily
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Image(
                        painter = painterResource(id = R.drawable.editar),
                        contentDescription = "Editar perfil",
                        modifier = Modifier.size(24.dp)
                    )
                }

                Button(
                    onClick = {
                        authService.logoutUser {
                            navController.navigate(ManejoDeLasPantallasDeLaApp.PantallaConInfoApp.ruta)
                            Toast.makeText(context, "Sesión cerrada", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray)
                ) {
                    Text(
                        text = "Cerrar Sesión",
                        color = Color.White,
                        fontFamily = fontFamily
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Image(
                        painter = painterResource(id = R.drawable.cerrarsesion),
                        contentDescription = "Cerrar sesión",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            // Botón para eliminar cuenta
            Button(
                onClick = {
                    showDeleteConfirmation = true
                    isDeleteButtonEnabled = false
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text(
                    text = "Eliminar Cuenta",
                    color = Color.White,
                    fontFamily = fontFamily
                )
                Spacer(modifier = Modifier.width(8.dp))
                Image(
                    painter = painterResource(id = R.drawable.eliminar),
                    contentDescription = "Eliminar cuenta",
                    modifier = Modifier.size(24.dp)
                )
            }

            // Diálogo de confirmación de eliminación de cuenta
            if (showDeleteConfirmation) {
                confirmarEliminacionDialog(
                    timerText = timerText,
                    isDeleteButtonEnabled = isDeleteButtonEnabled,
                    onCancel = { showDeleteConfirmation = false },
                    onDelete = {
                        firestoreService.deleteUserFromFirestore(user!!) {
                            if (it) {
                                Toast.makeText(context, "Cuenta eliminada con éxito", Toast.LENGTH_SHORT).show()
                                navController.popBackStack()
                            } else {
                                Toast.makeText(context, "Error al eliminar la cuenta", Toast.LENGTH_SHORT).show()
                            }
                            showDeleteConfirmation = false
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun confirmarEliminacionDialog(
    timerText: String,
    isDeleteButtonEnabled: Boolean,
    onCancel: () -> Unit,
    onDelete: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onCancel,
        title = { Text(text = "Confirmar eliminación de cuenta") },
        text = { Text(text = "¿Estás seguro de que deseas eliminar tu cuenta? Esta acción no se puede deshacer.") },
        confirmButton = {
            Button(
                onClick = onDelete,
                enabled = isDeleteButtonEnabled,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text(text = timerText, color = Color.White)
            }
        },
        dismissButton = {
            Button(onClick = onCancel) {
                Text(text = "Cancelar")
            }
        }
    )
}

// Función para validar los campos
fun validarCampos(
    nombres: String,
    apellidos: String,
    telefono: String,
    cui: String,
    context: android.content.Context
): Boolean {
    if (!Validaciones.isValidName(nombres)) {
        Toast.makeText(context, "Nombres inválidos", Toast.LENGTH_SHORT).show()
        return false
    }
    if (!Validaciones.isValidName(apellidos)) {
        Toast.makeText(context, "Apellidos inválidos", Toast.LENGTH_SHORT).show()
        return false
    }
    if (!Validaciones.isValidPhone(telefono)) {
        Toast.makeText(context, "Teléfono inválido", Toast.LENGTH_SHORT).show()
        return false
    }
    if (!Validaciones.isValidCUI(cui)) {
        Toast.makeText(context, "CUI inválido", Toast.LENGTH_SHORT).show()
        return false
    }
    return true
}
