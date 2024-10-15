package com.crissnm.registrousuarios.Componentes.Inicio

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
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
    // Estado para controlar la habilitación/deshabilitación del botón
    var isButtonEnabled by rememberSaveable { mutableStateOf(true) }

    // Estado para mostrar los diálogos
    var showAlertDialog by remember { mutableStateOf(false) }
    var showConfirmationDialog by remember { mutableStateOf(false) }

    // Estado para la ubicación
    var latitud by remember { mutableStateOf(0.0) }
    var longitud by remember { mutableStateOf(0.0) }

    // Botón de alerta
    Button(
        onClick = {
            if (isButtonEnabled) {
                // Comprobar permisos y obtener la ubicación
                if (permissionsState.allPermissionsGranted) {
                    getCurrentLocation(fusedLocationClient) { lat, lon ->
                        latitud = lat
                        longitud = lon
                        // Mostrar la ubicación en un Toast
                        Toast.makeText(context, "Latitud: $lat, Longitud: $lon", Toast.LENGTH_SHORT).show()

                        // Mostramos el diálogo para ingresar detalles
                        showAlertDialog = true
                    }
                } else {
                    Toast.makeText(context, "Permiso de ubicación no concedido.", Toast.LENGTH_SHORT).show()
                }
            }
        },
        enabled = isButtonEnabled, // Control del estado del botón
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

    // Diálogo para ingresar detalles de la alerta
    if (showAlertDialog) {
        showAlertInputDialog(
            latitud = latitud,
            longitud = longitud,
            onSend = {
                showAlertDialog = false
                showConfirmationDialog = true // Muestra el segundo diálogo
            },
            onDismiss = { showAlertDialog = false }
        )
    }

    // Diálogo de confirmación
    if (showConfirmationDialog) {
        showConfirmationDialog(
            onAccept = {
                showConfirmationDialog = false
                isButtonEnabled = false // Deshabilitar el botón tras enviar la alerta
            }
        )
    }
}

@Composable
fun showAlertInputDialog(
    latitud: Double,
    longitud: Double,
    onSend: () -> Unit,
    onDismiss: () -> Unit
) {
    var alertDetails by remember { mutableStateOf("") }

    androidx.compose.material3.AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(text = "Alerta Recibida!") },
        text = {
            Column {
                Text("Latitud: $latitud, Longitud: $longitud") // Mostrar ubicación
                Text("Puedes darnos más detalles sobre la situación?")
                Spacer(modifier = Modifier.height(8.dp))
                androidx.compose.material3.OutlinedTextField(
                    value = alertDetails,
                    onValueChange = { alertDetails = it },
                    label = { Text("Detalles") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(onClick = { onSend() }) {
                Text("Enviar")
            }
        },
        dismissButton = {
            Button(onClick = { onDismiss() }) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
fun showConfirmationDialog(onAccept: () -> Unit) {
    androidx.compose.material3.AlertDialog(
        onDismissRequest = { },
        title = { Text(text = "Información Enviada") },
        text = { Text("Información enviada con éxito, revisa el apartado de notificaciones para tener información sobre el estado de tu alerta.") },
        confirmButton = {
            Button(onClick = { onAccept() }) {
                Text("Aceptar")
            }
        }
    )
}

@SuppressLint("MissingPermission")
private fun getCurrentLocation(fusedLocationClient: FusedLocationProviderClient, onLocationResult: (Double, Double) -> Unit) {
    fusedLocationClient.lastLocation.addOnSuccessListener { location ->
        if (location != null) {
            onLocationResult(location.latitude, location.longitude)
        } else {
            onLocationResult(0.0, 0.0)
        }
    }
}    