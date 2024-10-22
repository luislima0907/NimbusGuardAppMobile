package com.crissnm.registrousuarios.Navegacion

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.crissnm.registrousuarios.ManejoDeUsuarios.User
import com.crissnm.registrousuarios.ManejoDeUsuarios.UserAuthService
import com.crissnm.registrousuarios.PantallasDeLaApp.PantallaDeBienvenida
import com.crissnm.registrousuarios.PantallasDeLaApp.PantallaDeInicio
import com.crissnm.registrousuarios.PantallasDeLaApp.PantallaConInfoApp
import com.crissnm.registrousuarios.PantallasDeLaApp.PantallaDeLogin
import com.crissnm.registrousuarios.PantallasDeLaApp.PantallaDeNotificacion
import com.crissnm.registrousuarios.PantallasDeLaApp.PantallaDePerfil
import com.crissnm.registrousuarios.PantallasDeLaApp.PantallaDeRegistro
import com.crissnm.registrousuarios.PantallasDeLaApp.PantallaDetalleDeLaAlerta
import com.crissnm.registrousuarios.PantallasDeLaApp.PantallaPrincipal

@Composable
fun navegacionDeLaApp() {
    val navController = rememberNavController()
    val authService = UserAuthService()

    // Crear un mapa de estado para controlar la habilitación de los botones
    val buttonStates = rememberSaveable { mutableStateOf(mutableMapOf<String, Boolean>()) }

    NavHost(navController = navController, startDestination = ManejoDeLasPantallasDeLaApp.PantallaDeBienvenida.ruta) {
        composable(route = ManejoDeLasPantallasDeLaApp.PantallaPrincipal.ruta + "/{uid}",
            arguments = listOf(navArgument("uid") { type = NavType.StringType })) {
            PantallaPrincipal(navController, buttonStates.value, onButtonStatusChange = { buttonId, isEnabled ->
                buttonStates.value[buttonId] = isEnabled
            })
        }
        composable(route = ManejoDeLasPantallasDeLaApp.PantallaDeRegistro.ruta) {
            PantallaDeRegistro(navController)
        }
        composable(route = ManejoDeLasPantallasDeLaApp.PantallaDeLogin.ruta) {
            PantallaDeLogin(navController = navController, users = listOf())
        }
        composable(
            route = ManejoDeLasPantallasDeLaApp.PantallaDeInicio.ruta + "/{uid}",
            arguments = listOf(navArgument("uid") { type = NavType.StringType })
        ) {
            PantallaDeInicio(navController, buttonStates.value, onButtonStatusChange = { buttonId, isEnabled ->
                buttonStates.value[buttonId.toString()] = isEnabled as Boolean
            })
        }
        composable(
            route = ManejoDeLasPantallasDeLaApp.PantallaConInfoApp.ruta){
            PantallaConInfoApp(navController)
        }
        composable(route = ManejoDeLasPantallasDeLaApp.PantallaDetalleDeLaAlerta.ruta) {
            PantallaDetalleDeLaAlerta(navController)
        }
        composable(route = ManejoDeLasPantallasDeLaApp.PantallaDeNotificacion.ruta) {
            PantallaDeNotificacion(navController)
        }
        composable(route = ManejoDeLasPantallasDeLaApp.PantallaDePerfil.ruta) {
            val newUser = User(
                uid = "",
                name = "",
                lastname = "",
                email = "",
                password = "",
                cui = "",
                number = "",
                department = "",
                municipality = ""
            )
            PantallaDePerfil(navController = navController,user = newUser, authService = authService) // Pasando el usuario
        }
        composable(route = ManejoDeLasPantallasDeLaApp.PantallaDeBienvenida.ruta){
            PantallaDeBienvenida(navController)
        }
    }
}
