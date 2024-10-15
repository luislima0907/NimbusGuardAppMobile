package com.crissnm.registrousuarios.Componentes.Inicio

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.crissnm.registrousuarios.Navegacion.ManejoDeLasPantallasDeLaApp
import com.crissnm.registrousuarios.PantallasDeLaApp.contenidoDeLaBarraDeNavegacionInferior
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.android.gms.location.FusedLocationProviderClient

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun BotonDeAlerta(
    navController: NavController,
    buttonConfig: ButtonConfig,
    fusedLocationClient: FusedLocationProviderClient,
    permissionsState: MultiplePermissionsState,
    context: Context
) {
    Button(
        onClick = {
            // Comprobar permisos y obtener la ubicación
            if (permissionsState.allPermissionsGranted) {
                getCurrentLocation(fusedLocationClient) { lat, lon ->
                    // Mostrar la ubicación en un Toast
                    Toast.makeText(context, "Latitud: $lat, Longitud: $lon", Toast.LENGTH_SHORT).show()
                }
                //navController.navigate(ManejoDeLasPantallasDeLaApp.PantallaDetalleDeLaAlerta.ruta)
                //contenidoDeLaBarraDeNavegacionInferior(modifier = Modifier, selectedIndex = 3)
            } else {
                Toast.makeText(context, "Permiso de ubicación no concedido.", Toast.LENGTH_SHORT).show()
            }
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = buttonConfig.containerColor,
            contentColor = buttonConfig.contentColor
        ),
        modifier = Modifier
            .size(buttonConfig.widthButton, buttonConfig.heightButton)
            .padding(buttonConfig.paddingButton)
    ) {
        Image(
            painter = painterResource(id = buttonConfig.imageId),
            contentDescription = null,
            modifier = Modifier.size(buttonConfig.sizeImage)
        )
        Text(
            textAlign = TextAlign.Center,
            text = buttonConfig.title,
            color = buttonConfig.colorTitle,
            fontSize = buttonConfig.fontSizeTitle,
            modifier = Modifier.padding(buttonConfig.paddingTitle)
        )
    }
}

@SuppressLint("MissingPermission")
private fun getCurrentLocation(fusedLocationClient: FusedLocationProviderClient, onLocationResult: (Double, Double) -> Unit) {
    fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
        // Puede que location sea null si no hay ubicación disponible
        if (location != null) {
            onLocationResult(location.latitude, location.longitude)
        } else {
            // Llama a onLocationResult con valores nulos si no se encuentra la ubicación
            onLocationResult(0.0, 0.0) // Ajusta según tu lógica
        }
    }
}