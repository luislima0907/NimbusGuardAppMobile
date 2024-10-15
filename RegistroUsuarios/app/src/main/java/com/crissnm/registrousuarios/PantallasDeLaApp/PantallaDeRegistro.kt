package com.crissnm.registrousuarios.PantallasDeLaApp

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
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
import com.crissnm.registrousuarios.ManejoDeUsuarios.User
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
    val users = remember { mutableStateListOf<User>() }
    val correo = remember { mutableStateOf("") }
    val contrasena = remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, top = 60.dp, bottom = 10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
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
        ContrasenaField(contrasena)

        Spacer(modifier = Modifier.height(3.dp))

        RegisterUserButton(
            onClick = {
                // Verificar si algún campo está vacío antes de proceder
                when {
                    nombres.value.isBlank() -> {
                        Toast.makeText(
                            context,
                            "El campo de nombres está vacío",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    apellidos.value.isBlank() -> {
                        Toast.makeText(
                            context,
                            "El campo de apellidos está vacío",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    cui.value.isBlank() -> {
                        Toast.makeText(context, "El campo de CUI está vacío", Toast.LENGTH_SHORT)
                            .show()
                    }

                    telefono.value.isBlank() -> {
                        Toast.makeText(
                            context,
                            "El campo de teléfono está vacío",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    correo.value.isBlank() -> {
                        Toast.makeText(context, "El campo de correo está vacío", Toast.LENGTH_SHORT)
                            .show()
                    }

                    contrasena.value.isBlank() -> {
                        Toast.makeText(
                            context,
                            "El campo de contraseña está vacío",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    else -> {
                        // Si todos los campos están completos, proceder con el registro
                        val newUser = User(
                            nombres.value,
                            apellidos.value,
                            cui.value,
                            telefono.value,
                            departamento.value,
                            municipio.value,
                            correo.value,
                            contrasena.value
                        )
                        users.add(newUser)
//                        Toast.makeText(context, "Registro exitoso", Toast.LENGTH_SHORT).show()
                        navController.navigate(ManejoDeLasPantallasDeLaApp.PantallaPrincipal.ruta)
                    }
                }
            },
            nombres = nombres,
            apellidos = apellidos,
            cui = cui,
            telefono = telefono,
            email = correo,
            contrasena = contrasena
        )
    }
}


//@Composable
//fun RegistrationForm(navController: NavController) {
//    val nombres = remember { mutableStateOf("") }
//    val apellidos = remember { mutableStateOf("") }
//    val cui = remember { mutableStateOf("") }
//    val telefono = remember { mutableStateOf("") }
//    val departamento = remember { mutableStateOf("") }
//    val municipio = remember { mutableStateOf("") }
//    val users = remember { mutableStateListOf<User>() }
//    val correo = remember { mutableStateOf("") }
//    val contrasena = remember { mutableStateOf("") }
//
//
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(start = 20.dp, end = 20.dp, top = 60.dp, bottom = 10.dp),
//        verticalArrangement = Arrangement.spacedBy(10.dp)
//
//    ){
//
//        Titulo()
//        ImagenRegis()
//        NombreField(nombres)
//        ApellidoField(apellidos)
//        CUIField(cui, municipio, departamento)
//        TelefonoField(telefono)
//        DepartamentoField(departamento)
//        MunicipioField(municipio)
//        CorreoField(correo)
//        ContrasenaField(contrasena)
//
//        Spacer(modifier = Modifier.height(3.dp))
//
//        RegisterUserButton(
//            onClick = {
//                navController.navigate(ManejoDeLasPantallasDeLaApp.PantallaPrincipal.ruta)
//            }
//        ) {
//            val newUser = User(
//                nombres.value,
//                apellidos.value,
//                cui.value,
//                telefono.value,
//                departamento.value,
//                municipio.value,
//                correo.value,
//                contrasena.value
//            )
//            users.add(newUser)
//        }
//    }
//}