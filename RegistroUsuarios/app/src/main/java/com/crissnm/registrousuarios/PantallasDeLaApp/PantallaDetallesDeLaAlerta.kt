package com.crissnm.registrousuarios.PantallasDeLaApp

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PantallaDetalleDeLaAlerta(navController: NavController){
        contenidoPantallaDetallesDeLaAlerta(navController)
}

@Composable
fun contenidoPantallaDetallesDeLaAlerta(navController: NavController){
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Reporte recibido")
        //cuando se le de click al boton, mostrar un mensaje tipo toast para indicarle
        // que sus detalles fueron enviados correctamente y que vuelva a la pantalla anterior
        Button(onClick = {
            navController.popBackStack()
        }) {
            Text("Enviar detalles")
        }
    }
}