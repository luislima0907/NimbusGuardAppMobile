package com.crissnm.registrousuarios.PantallasDeLaApp

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import com.crissnm.registrousuarios.Componentes.Registro.ApellidoField
import com.crissnm.registrousuarios.Componentes.Registro.CUIField
import com.crissnm.registrousuarios.Componentes.Registro.ContrasenaField
import com.crissnm.registrousuarios.Componentes.Registro.CorreoField
import com.crissnm.registrousuarios.Componentes.Registro.DepartamentoField
import com.crissnm.registrousuarios.Componentes.Registro.ImagenRegis
import com.crissnm.registrousuarios.Componentes.Registro.MunicipioField
import com.crissnm.registrousuarios.Componentes.Registro.NombreField
import com.crissnm.registrousuarios.Componentes.Registro.RegisterUserButton
import com.crissnm.registrousuarios.Componentes.Registro.TelefonoField
import com.crissnm.registrousuarios.Componentes.Registro.Titulo
import com.crissnm.registrousuarios.Componentes.Registro.validarApellido
import com.crissnm.registrousuarios.Componentes.Registro.validarCUI
import com.crissnm.registrousuarios.Componentes.Registro.validarCorreo
import com.crissnm.registrousuarios.Componentes.Registro.validarNombre
import com.crissnm.registrousuarios.Componentes.Registro.validarTelefono
import com.crissnm.registrousuarios.ManejoDeUsuarios.User
import com.crissnm.registrousuarios.ManejoDeUsuarios.UserAuthService
import com.crissnm.registrousuarios.ManejoDeUsuarios.Validaciones
import com.crissnm.registrousuarios.Navegacion.ManejoDeLasPantallasDeLaApp

@Composable
fun PantallaDeRegistro(navController: NavController) {
    RegistrationForm(navController)
}

@Composable
fun RegistrationForm(navController: NavController) {
    val nombres = remember { mutableStateOf("") }
    val apellidos = remember { mutableStateOf("") }
    val cui = remember { mutableStateOf("") }
    val telefono = remember { mutableStateOf("") }
    val departamento = remember { mutableStateOf("") }
    val municipio = remember { mutableStateOf("") }
    val correo = remember { mutableStateOf("") }
    val contrasena = remember { mutableStateOf("") }
    val errorMessage = remember { mutableStateOf("") }
    val context = LocalContext.current

    // Estado para controlar el diálogo de éxito
    val showSuccessDialog = remember { mutableStateOf(false) }

    val scrollState = rememberScrollState() // Estado para el scroll

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 20.dp, end = 20.dp, top = 90.dp, bottom = 10.dp)
            .verticalScroll(scrollState), // Habilitar desplazamiento
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Titulo()
        ImagenRegis()
        NombreField(nombres)
        ApellidoField(apellidos)
        CUIField(cui, municipio, departamento)
        TelefonoField(telefono)
        DepartamentoField(departamento)
        MunicipioField(municipio)
        CorreoField(correo)
        ContrasenaField(contrasena, errorMessage)

        Spacer(modifier = Modifier.height(3.dp))

        RegisterUserButton(
            onClick = {
                showSuccessDialog.value = true
            },
            nombres = nombres,
            apellidos = apellidos,
            cui = cui,
            telefono = telefono,
            email = correo,
            contrasena = contrasena,
            departamento = departamento,
            municipio = municipio
        )
    }

    // Mostrar el diálogo si `showSuccessDialog` está en true
    if (showSuccessDialog.value) {
        AlertDialog(
            onDismissRequest = { showSuccessDialog.value = false },
            title = { Text(text = "Registro Exitoso!", fontWeight = FontWeight.Bold, fontSize = 20.sp) },
            text = {
                Column {
                    Text(text = "Datos registrados:")
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = "Nombre: ${nombres.value}")
                    Text(text = "Apellidos: ${apellidos.value}")
                    Text(text = "CUI: ${maskField(cui.value)}")
                    Text(text = "Teléfono: ${maskField(telefono.value)}")
                    Text(text = "Correo: ${maskField(correo.value)}")
                    Text(text = "Contraseña: ${maskAllField(contrasena.value)}")
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        handleUserRegistration(
                            nombres =  nombres.value,
                            apellidos =  apellidos.value,
                            cui =  cui.value,
                            telefono =  telefono.value,
                            correo =  correo.value,
                            contrasena =  contrasena.value,
                            departamento =  departamento.value,
                            municipio =  municipio.value,
                            context =  context,
                            navController =  navController
                        ) {
                            showSuccessDialog.value = false
                        }
                    }
                ) {
                    Text("Aceptar")
                }
            },
            properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
        )
    }
}

fun handleUserRegistration(
    nombres: String,
    apellidos: String,
    cui: String,
    telefono: String,
    correo: String,
    contrasena: String,
    departamento: String,
    municipio: String,
    context: Context,
    navController: NavController,
    onRegistrationComplete: () -> Unit
) {
    val newUser = User(
        uid = "",
        name = nombres,
        lastname = apellidos,
        email = correo,
        password = contrasena,
        cui = cui,
        number = telefono,
        department = departamento,
        municipality = municipio
    )

    val userAuthService = UserAuthService()

    userAuthService.registerUser(context, newUser) { uid ->
        if (!uid.isNullOrEmpty()) {
            navController.navigate(
                "${ManejoDeLasPantallasDeLaApp.PantallaPrincipal.ruta}/$uid"
            )
        }
        onRegistrationComplete() // Cerrar el diálogo después de la navegación
    }
}


// Función para enmascarar parte de la información sensible
fun maskField(field: String): String {
    val visibleLength = field.length / 2
    val hiddenPart = "*".repeat(field.length - visibleLength)
    return field.take(visibleLength) + hiddenPart
}

// Función para ocultar completamente la contraseña
fun maskAllField(field: String): String {
    return "*".repeat(field.length)
}