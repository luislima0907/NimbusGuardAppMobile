package com.crissnm.registrousuarios.PantallasDeLaApp

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.crissnm.registrousuarios.Componentes.pantallainicial.fontFamily
import com.crissnm.registrousuarios.DepYmuni.ValidarCUI
import com.crissnm.registrousuarios.ManejoDeUsuarios.UserAuthService
import com.crissnm.registrousuarios.ManejoDeUsuarios.UserFireStoreService
import com.crissnm.registrousuarios.ManejoDeUsuarios.UserProfileViewModel
import com.crissnm.registrousuarios.ManejoDeUsuarios.UserProfileViewModelFactory
import com.crissnm.registrousuarios.R

@Composable
fun PasswordEmail(navController: NavController, uid: String, authService: UserAuthService) {
    // Obtener instancia del ViewModel
    val userProfileViewModel: UserProfileViewModel = viewModel(
        factory = UserProfileViewModelFactory(UserFireStoreService(), ValidarCUI())
    )

    // Cargar los datos del usuario si no se han cargado
    LaunchedEffect(uid) {
        userProfileViewModel.loadUser(uid)
    }

    cambioDeContrasenayEmail(
        navController = navController,
        userProfileViewModel = userProfileViewModel,
        uid = uid,
        authService = authService
    )
}

@Composable
fun cambioDeContrasenayEmail(
    navController: NavController,
    userProfileViewModel: UserProfileViewModel,
    uid: String,
    authService: UserAuthService
) {
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var isEditable by remember { mutableStateOf(false) }

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(start = 20.dp, end = 20.dp, top = 180.dp, bottom = 10.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Spacer(modifier = Modifier.height(70.dp))
        Text("Cambiar Contraseña y Correo", fontSize = 28.sp,
            modifier = Modifier.padding(bottom = 12.dp),
            textAlign = TextAlign.Center,
            fontFamily = fontFamily,
        )
        OutlinedTextField(
            value = correo,
            onValueChange = { correo = it },
            label = { Text("Correo") },
            readOnly = !isEditable,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = contrasena,
            onValueChange = { contrasena = it },
            label = { Text("Contraseña") },
            readOnly = !isEditable,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (isEditable) {
//                    // Aquí se guardan los cambios
                }
                isEditable = !isEditable
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (isEditable) "Guardar cambios" else "Editar",
                fontFamily = fontFamily
            )
            Spacer(modifier = Modifier.width(8.dp))
            Image(
                painter = painterResource(id = R.drawable.editar),
                contentDescription = "Editar perfil",
                modifier = Modifier.size(24.dp)
            )

        }
    }
}
