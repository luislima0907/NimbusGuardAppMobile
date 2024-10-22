package com.crissnm.registrousuarios.PantallasDeLaApp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.crissnm.registrousuarios.Componentes.pantallainicial.ActionButtons
import com.crissnm.registrousuarios.Componentes.pantallainicial.InstruccionesUso
import com.crissnm.registrousuarios.Componentes.pantallainicial.PhotoCarousel
import com.crissnm.registrousuarios.Componentes.pantallainicial.TextoBienvenida

@Composable
fun PantallaConInformacionDeLaApp(navController: NavController) {
    contenidoPantallaConInformacionDeLaApp(navController)
}

@Composable
fun contenidoPantallaConInformacionDeLaApp(navController: NavController) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextoBienvenida()
        Spacer(modifier = Modifier.height(16.dp))
        InstruccionesUso()
        Spacer(modifier = Modifier.height(18.dp))
        PhotoCarousel()
        Spacer(modifier = Modifier.height(16.dp))
        ActionButtons()
    }
}
