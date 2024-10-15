package com.crissnm.registrousuarios.Navegacion

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.crissnm.registrousuarios.PantallasDeLaApp.PantallaDeInicio
import com.crissnm.registrousuarios.PantallasDeLaApp.PantallaDeLogin
import com.crissnm.registrousuarios.PantallasDeLaApp.PantallaDeNotificacion
import com.crissnm.registrousuarios.PantallasDeLaApp.PantallaDePerfil
import com.crissnm.registrousuarios.PantallasDeLaApp.PantallaDeRegistro
import com.crissnm.registrousuarios.PantallasDeLaApp.PantallaDetalleDeLaAlerta
import com.crissnm.registrousuarios.PantallasDeLaApp.PantallaPrincipal
import com.crissnm.registrousuarios.PantallasDeLaApp.barraDeNavegacionInferior

@Composable
fun navegacionDeLaApp() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = ManejoDeLasPantallasDeLaApp.PantallaDeRegistro.ruta){
        composable(route = ManejoDeLasPantallasDeLaApp.PantallaPrincipal.ruta) {
            PantallaPrincipal(navController)
        }
        composable(route = ManejoDeLasPantallasDeLaApp.PantallaDeRegistro.ruta) {
            PantallaDeRegistro(navController)
        }
        composable(route = ManejoDeLasPantallasDeLaApp.PantallaDeLogin.ruta) {
            PantallaDeLogin(navController)
        }
        composable(route = ManejoDeLasPantallasDeLaApp.PantallaDeInicio.ruta) {
            PantallaDeInicio(navController)
        }
        composable(route = ManejoDeLasPantallasDeLaApp.PantallaDetalleDeLaAlerta.ruta){
            PantallaDetalleDeLaAlerta(navController)
        }
        composable(route = ManejoDeLasPantallasDeLaApp.PantallaDeNotificacion.ruta) {
            PantallaDeNotificacion(navController)
        }
        composable(route = ManejoDeLasPantallasDeLaApp.PantallaDePerfil.ruta) {
            PantallaDePerfil(navController)
        }
    }
}