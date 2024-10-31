package com.crissnm.registrousuarios.PantallasDeLaApp

import android.Manifest
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.crissnm.registrousuarios.Componentes.Inicio.BotonDeAlerta
import com.crissnm.registrousuarios.Componentes.Inicio.BotonDeAlertaViewModel
import com.crissnm.registrousuarios.Componentes.Inicio.BotonDeAlertaViewModelFactory
import com.crissnm.registrousuarios.Componentes.Inicio.ButtonBuilder
import com.crissnm.registrousuarios.Componentes.pantallainicial.fontFamily
import com.crissnm.registrousuarios.R
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.LocationServices

@Composable
fun PantallaDeInicio(
    navController: NavController,
    buttonStates: Map<String, Boolean>,
    onButtonStatusChange: (String, Boolean) -> Unit
) {
    val context = LocalContext.current
    val viewModel: BotonDeAlertaViewModel = viewModel(factory = BotonDeAlertaViewModelFactory(context))
    val _buttonStates by viewModel.buttonStates.collectAsState(initial = emptyMap())

    contenidoPantallaDeInicio(
        navController = navController,
        buttonStates = _buttonStates,
        onButtonStatusChange = onButtonStatusChange,
        viewModel = viewModel
    )
}

@OptIn(ExperimentalPermissionsApi::class)
@SuppressLint("SuspiciousIndentation")
@Composable
fun contenidoPantallaDeInicio(
    navController: NavController,
    buttonStates: Map<String, Boolean>,
    onButtonStatusChange: (String, Boolean) -> Unit,
    viewModel: BotonDeAlertaViewModel
) {

    val uid = navController.currentBackStackEntry?.arguments?.getString("uid")
    var alertId = navController.currentBackStackEntry?.arguments?.getString("alertId")
    val context = LocalContext.current
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    val permissionsState = rememberMultiplePermissionsState(
        permissions = listOf(Manifest.permission.ACCESS_FINE_LOCATION)
    )

    LaunchedEffect(Unit) {
        permissionsState.launchMultiplePermissionRequest()
    }

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

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Presiona un Botón para Enviar una Alerta",
            color = Color.Black,
            fontFamily = fontFamily,
            fontSize = 20.sp
        )
        buttonConfigs.forEachIndexed { index, buttonConfig ->
            val buttonId = "alertButton_$index" // ID único para cada botón

            BotonDeAlerta(
                uid = uid.toString(),
                idAlert = alertId.toString(),
                buttonId = buttonId,
                navController = navController,
                buttonConfig = buttonConfig,
                fusedLocationClient = fusedLocationClient,
                permissionsState = permissionsState,
                context = context,
                onButtonStatusChange = { isEnabled ->
                    if (!isEnabled) {
                        viewModel.disableButton(buttonId)
                        onButtonStatusChange(buttonId, isEnabled)
                    }
                    //viewModel.disableButton(buttonId)
                }
            )
        }
    }
}