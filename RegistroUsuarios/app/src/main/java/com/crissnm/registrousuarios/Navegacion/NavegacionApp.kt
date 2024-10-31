package com.crissnm.registrousuarios.Navegacion

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.crissnm.registrousuarios.Componentes.Inicio.BotonDeAlertaViewModel
import com.crissnm.registrousuarios.Componentes.Inicio.BotonDeAlertaViewModelFactory
import com.crissnm.registrousuarios.ManejoDeUsuarios.UserAuthService
import com.crissnm.registrousuarios.PantallasDeLaApp.PantallaConInfoApp
import com.crissnm.registrousuarios.PantallasDeLaApp.PantallaDeBienvenida
import com.crissnm.registrousuarios.PantallasDeLaApp.PantallaDeInicio
import com.crissnm.registrousuarios.PantallasDeLaApp.PantallaDeLogin
import com.crissnm.registrousuarios.PantallasDeLaApp.PantallaDeNotificacion
import com.crissnm.registrousuarios.PantallasDeLaApp.PantallaDePerfil
import com.crissnm.registrousuarios.PantallasDeLaApp.PantallaDeRegistro
import com.crissnm.registrousuarios.PantallasDeLaApp.PantallaPrincipal

import kotlinx.coroutines.launch


@Composable
fun navegacionDeLaApp() {
    val navController = rememberNavController()
    val authService = UserAuthService()

    val context = navController.context
    val viewModel: BotonDeAlertaViewModel = viewModel(factory = BotonDeAlertaViewModelFactory(context))
    val buttonStates by viewModel.buttonStates.collectAsState(initial = emptyMap())

    NavHost(navController = navController, startDestination = ManejoDeLasPantallasDeLaApp.PantallaDeBienvenida.ruta) {
        composable(route = ManejoDeLasPantallasDeLaApp.PantallaPrincipal.ruta + "/{uid}",
            arguments = listOf(navArgument("uid") { type = NavType.StringType })) {
            PantallaPrincipal(navController, buttonStates, onButtonStatusChange = { buttonId, isEnabled ->
                viewModel.viewModelScope.launch {
                    viewModel.updateButtonState(buttonId, isEnabled)
                }
            })
        }
        composable(route = ManejoDeLasPantallasDeLaApp.PantallaDeInicio.ruta + "/{uid}",
            arguments = listOf(navArgument("uid") { type = NavType.StringType })) {
            PantallaDeInicio(navController, buttonStates, onButtonStatusChange = { buttonId, isEnabled ->
                viewModel.viewModelScope.launch {
                    viewModel.updateButtonState(buttonId, isEnabled)
                }
            })
        }
        composable(route = ManejoDeLasPantallasDeLaApp.PantallaDeRegistro.ruta) {
            PantallaDeRegistro(navController)
        }
        composable(route = ManejoDeLasPantallasDeLaApp.PantallaDeLogin.ruta) {
            PantallaDeLogin(navController = navController, users = listOf())
        }
        composable(
            route = ManejoDeLasPantallasDeLaApp.PantallaConInfoApp.ruta){
            PantallaConInfoApp(navController)
        }
        composable(route = ManejoDeLasPantallasDeLaApp.PantallaDeNotificacion.ruta) {
            PantallaDeNotificacion(navController =  navController)
        }
        composable(
            route = ManejoDeLasPantallasDeLaApp.PantallaDePerfil.ruta + "/{uid}",
            arguments = listOf(navArgument("uid") { type = NavType.StringType })
        ) {
            val uid = it.arguments?.getString("uid") ?: ""
            PantallaDePerfil(navController = navController, uid = uid, authService = authService)
        }
        composable(route = ManejoDeLasPantallasDeLaApp.PantallaDeBienvenida.ruta){
            PantallaDeBienvenida(navController)
        }
        composable(route = ManejoDeLasPantallasDeLaApp.PantallaConInfoApp.ruta){
            PantallaConInfoApp(navController)
        }
    }
}
