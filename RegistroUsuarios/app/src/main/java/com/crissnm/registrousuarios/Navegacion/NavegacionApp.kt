package com.crissnm.registrousuarios.Navegacion

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.crissnm.registrousuarios.ManejoDeUsuarios.User
import com.crissnm.registrousuarios.PantallasDeLaApp.PantallaDeBienvenida
import com.crissnm.registrousuarios.PantallasDeLaApp.PantallaDeInicio
import com.crissnm.registrousuarios.PantallasDeLaApp.PantallaDeLogin
import com.crissnm.registrousuarios.PantallasDeLaApp.PantallaDeNotificacion
import com.crissnm.registrousuarios.PantallasDeLaApp.PantallaDePerfil
import com.crissnm.registrousuarios.PantallasDeLaApp.PantallaDeRegistro
import com.crissnm.registrousuarios.PantallasDeLaApp.PantallaDetalleDeLaAlerta
import com.crissnm.registrousuarios.PantallasDeLaApp.PantallaPrincipal

@Composable
fun navegacionDeLaApp() {
    val navController = rememberNavController()

    // Crear un mapa de estado para controlar la habilitación de los botones
    val buttonStates = rememberSaveable { mutableStateOf(mutableMapOf<String, Boolean>()) }

    NavHost(navController = navController, startDestination = ManejoDeLasPantallasDeLaApp.PantallaDeBienvenida.ruta) {
        composable(route = ManejoDeLasPantallasDeLaApp.PantallaPrincipal.ruta) {
            PantallaPrincipal(navController, buttonStates.value, onButtonStatusChange = { buttonId, isEnabled ->
                buttonStates.value[buttonId] = isEnabled
            })
        }
        composable(route = ManejoDeLasPantallasDeLaApp.PantallaDeRegistro.ruta) {
            PantallaDeRegistro(navController)
        }
        composable(route = ManejoDeLasPantallasDeLaApp.PantallaDeLogin.ruta) {
            PantallaDeLogin(navController = navController, users = listOf()) // Lista vacía para usuarios
        }
        composable(route = ManejoDeLasPantallasDeLaApp.PantallaDeInicio.ruta) {
            PantallaDeInicio(navController, buttonStates.value, onButtonStatusChange = { buttonId, isEnabled ->
                buttonStates.value[buttonId.toString()] = isEnabled as Boolean
            })
        }
        composable(route = ManejoDeLasPantallasDeLaApp.PantallaDetalleDeLaAlerta.ruta) {
            PantallaDetalleDeLaAlerta(navController)
        }
        composable(route = ManejoDeLasPantallasDeLaApp.PantallaDeNotificacion.ruta) {
            PantallaDeNotificacion(navController)
        }
        composable(route = ManejoDeLasPantallasDeLaApp.PantallaDePerfil.ruta) {
            val mockUser = User(
                nombres = "Juan",
                apellidos = "Pérez",
                cui = "123456789012",
                telefono = "1234 5678",
                departamento = "Guatemala",
                municipio = "Ciudad",
                correo = "juan.perez@example.com",
                contrasena = "password123"
            )
            PantallaDePerfil(navController = navController,user = mockUser) // Pasando el usuario simulado
        }
        composable(route = ManejoDeLasPantallasDeLaApp.PantallaDeBienvenida.ruta){
            PantallaDeBienvenida(navController)
        }
    }
}
