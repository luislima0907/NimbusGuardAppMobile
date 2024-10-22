package com.crissnm.registrousuarios.PantallasDeLaApp

import android.Manifest
import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.crissnm.registrousuarios.Componentes.Inicio.BotonDeAlerta
import com.crissnm.registrousuarios.Componentes.Inicio.ButtonBuilder
import com.crissnm.registrousuarios.R
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.LocationServices

@Composable
fun PantallaDeInicio(
    navController: NavController,
    buttonStates: MutableMap<String, Boolean>,
    onButtonStatusChange: (String, Boolean) -> Unit
) {
    contenidoPantallaDeInicio(
        navController = navController,
        buttonStates = buttonStates,
        onButtonStatusChange = onButtonStatusChange
    )
}

@OptIn(ExperimentalPermissionsApi::class)
@SuppressLint("SuspiciousIndentation")
@Composable
fun contenidoPantallaDeInicio(
    navController: NavController,
    buttonStates: MutableMap<String, Boolean>,
    onButtonStatusChange: (String, Boolean) -> Unit
) {
    val uid = navController.currentBackStackEntry?.arguments?.getString("uid")
    val context = LocalContext.current
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    val permissionsState = rememberMultiplePermissionsState(
        permissions = listOf(Manifest.permission.ACCESS_FINE_LOCATION)
    )

    LaunchedEffect(Unit) {
        permissionsState.launchMultiplePermissionRequest()
    }

    // Configuración de botones con identificadores únicos
    val buttonConfigs = listOf(
        ButtonBuilder()
            .setContainerColor(Color.Red)
            .setContentColor(Color.White)
            .setWidthButton(300.dp)
            .setHeightButton(100.dp)
            .setPaddingButton(10.dp)
            .setImageId(R.drawable.ambulance)
            .setSizeImage(60.dp)
            .setTitle("Urgencia Médica")
            .setColorTitle(Color.White)
            .setFontSizeTitle(20.sp)
            .setPaddingTitle(10.dp)
            .build(),
        ButtonBuilder()
            .setContainerColor(Color.Blue)
            .setContentColor(Color.White)
            .setWidthButton(300.dp)
            .setHeightButton(100.dp)
            .setPaddingButton(10.dp)
            .setImageId(R.drawable.police)
            .setSizeImage(60.dp)
            .setTitle("Délito o Intervención")
            .setColorTitle(Color.White)
            .setFontSizeTitle(20.sp)
            .setPaddingTitle(10.dp)
            .build(),
        ButtonBuilder()
            .setContainerColor(Color.Yellow)
            .setContentColor(Color.Black)
            .setWidthButton(300.dp)
            .setHeightButton(100.dp)
            .setPaddingButton(10.dp)
            .setImageId(R.drawable.fire_station)
            .setSizeImage(60.dp)
            .setTitle("Incendio")
            .setColorTitle(Color.Black)
            .setFontSizeTitle(20.sp)
            .setPaddingTitle(10.dp)
            .build(),
        ButtonBuilder()
            .setContainerColor(Color.DarkGray)
            .setContentColor(Color.White)
            .setWidthButton(300.dp)
            .setHeightButton(100.dp)
            .setPaddingButton(10.dp)
            .setImageId(R.drawable.grua)
            .setSizeImage(60.dp)
            .setTitle("Solicitud de Grua")
            .setColorTitle(Color.White)
            .setFontSizeTitle(20.sp)
            .setPaddingTitle(10.dp)
            .build(),
        ButtonBuilder()
            .setContainerColor(Color.Black)
            .setContentColor(Color.White)
            .setWidthButton(300.dp)
            .setHeightButton(100.dp)
            .setPaddingButton(10.dp)
            .setImageId(R.drawable.accidente_de_auto)
            .setSizeImage(60.dp)
            .setTitle("Accidente de Tráfico")
            .setColorTitle(Color.White)
            .setFontSizeTitle(20.sp)
            .setPaddingTitle(10.dp)
            .build()
    )

    // Inicializar el estado de los botones si aún no se ha hecho
    buttonConfigs.forEachIndexed { index, _ ->
        val buttonId = "alertButton_$index" // ID único para cada botón
        if (buttonStates[buttonId] == null) {
            buttonStates[buttonId] = true // Habilitar por defecto
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        buttonConfigs.forEachIndexed { index, buttonConfig ->
            val buttonId = "alertButton_$index" // ID único para cada botón
            //var isButtonEnabled = false

            BotonDeAlerta(
                id = uid.toString(),
                navController = navController,
                buttonConfig = buttonConfig,
                fusedLocationClient = fusedLocationClient,
                permissionsState = permissionsState,
                context = context,
                isButtonEnabled = buttonStates[buttonId] ?: true, // Verificar el estado del botón
                onButtonStatusChange = { isEnabled ->
                    buttonStates[buttonId] = isEnabled // Actualizar el estado del botón
                    //Log.d("ButtonState", "Button $buttonId isEnabled: $isEnabled") // Log del estado
                    onButtonStatusChange(buttonId, isEnabled) // Usar el ID como referencia
                }
            )
        }
    }
}
