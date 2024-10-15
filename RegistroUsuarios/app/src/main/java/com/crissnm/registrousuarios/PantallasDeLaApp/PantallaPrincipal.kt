package com.crissnm.registrousuarios.PantallasDeLaApp

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.crissnm.registrousuarios.Componentes.Inicio.ItemDeLaBarra

@Composable
fun PantallaPrincipal(navController: NavController){
    barraDeNavegacionInferior()
}

@Composable
fun barraDeNavegacionInferior() {
    val listaDeItemsDeLaBarraDeNavegacion = listOf(
        ItemDeLaBarra("Inicio", Icons.Default.Home),
        ItemDeLaBarra("Perfil", Icons.Default.Person),
        ItemDeLaBarra("Notificaciones", Icons.Default.Notifications)
    )

    var selectedIndex by remember { mutableStateOf(0) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                listaDeItemsDeLaBarraDeNavegacion.forEachIndexed {
                        index, itemDeLaBarra -> NavigationBarItem(
                    selected = selectedIndex == index,
                    onClick = {
                        selectedIndex = index
                    },
                    icon = {
                        Icon(
                            imageVector = itemDeLaBarra.icono,
                            contentDescription = "Icono"
                        )
                    },
                    label = {
                        Text(text = itemDeLaBarra.texto)
                    }
                )
                }
            }
        }
    ) {
            innerPadding -> contenidoDeLaBarraDeNavegacionInferior(
        modifier = Modifier.padding(innerPadding), selectedIndex
    )
    }
}

@Composable
fun contenidoDeLaBarraDeNavegacionInferior(modifier: Modifier, selectedIndex: Int) {
    val navController = rememberNavController()
    when(selectedIndex){
        //Aqui van las pantallas
        0 -> PantallaDeInicio(navController)
        1 -> PantallaDePerfil(navController)
        2 -> PantallaDeNotificacion(navController)
    }
}