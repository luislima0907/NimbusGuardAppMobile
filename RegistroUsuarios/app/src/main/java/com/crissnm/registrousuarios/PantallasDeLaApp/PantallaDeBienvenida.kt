package com.crissnm.registrousuarios.PantallasDeLaApp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.crissnm.registrousuarios.Navegacion.ManejoDeLasPantallasDeLaApp
import com.crissnm.registrousuarios.R
import com.crissnm.registrousuarios.R.color.celesteClaro
import kotlinx.coroutines.delay

@Composable
fun PantallaDeBienvenida(navController: NavController){
    LaunchedEffect(key1 = true) {
        delay(3000)
        navController.popBackStack()
        navController.navigate(ManejoDeLasPantallasDeLaApp.PantallaDeRegistro.ruta)
    }
    //navController.navigate(ManejoDeLasPantallasDeLaApp.PantallaDeRegistro.ruta)
    Splash()
}

@Composable
fun Splash(){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = celesteClaro)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.nimbusguard),
            contentDescription = "Logo NimbusGuard",
            modifier = Modifier.size(250.dp, 250.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PantallaDeBienvenidaPreview(){
    Splash()
}