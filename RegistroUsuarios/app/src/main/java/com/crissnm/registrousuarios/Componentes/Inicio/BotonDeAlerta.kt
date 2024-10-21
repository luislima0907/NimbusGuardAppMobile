package com.crissnm.registrousuarios.Componentes.Inicio

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.AlertDialog
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
import com.crissnm.registrousuarios.ManejoDeUsuarios.UserAuthService
import com.crissnm.registrousuarios.ManejoDeUsuarios.UserFireStoreService
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.android.gms.location.FusedLocationProviderClient
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun BotonDeAlerta(
    id:String,
    navController: NavController,
    buttonConfig: ButtonConfig,
    fusedLocationClient: FusedLocationProviderClient,
    permissionsState: MultiplePermissionsState,
    context: Context,
    isButtonEnabled: Boolean, // Estado habilitado
    onButtonStatusChange: (Boolean) -> Unit // Callback para cambiar el estado
) {

    // Estado para el botón
    var isButtonEnabled by rememberSaveable { mutableStateOf(isButtonEnabled) }
    // Estado para mostrar los diálogos
    var showAlertDialog by rememberSaveable { mutableStateOf(false) }
    var showConfirmationDialog by rememberSaveable { mutableStateOf(false) }

    // Estado para la ubicación
    var latitud by rememberSaveable { mutableStateOf(0.0) }
    var longitud by rememberSaveable { mutableStateOf(0.0) }

    // Botón de alerta
    Button(
        onClick = {
            if (isButtonEnabled) {
                if (permissionsState.allPermissionsGranted) {
                    getCurrentLocation(fusedLocationClient) { lat, lon ->
                        latitud = lat
                        longitud = lon
                        Toast.makeText(context, "Latitud: $lat, Longitud: $lon", Toast.LENGTH_SHORT).show()
                        showAlertDialog = true
                    }
                } else {
                    Toast.makeText(context, "Permiso de ubicación no concedido.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Este botón está deshabilitado hasta que atendamos tu caso.", Toast.LENGTH_SHORT).show()
            }
        },
        enabled = isButtonEnabled, // Usa el estado pasado
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
            uid = id,
            onSend = {
                showAlertDialog = false
                showConfirmationDialog = true
            },
            onDismiss = { showAlertDialog = false }
        )
    }

    // Diálogo de confirmación
    if (showConfirmationDialog) {
        showConfirmationDialog(
            onAccept = {
                showConfirmationDialog = false
                isButtonEnabled = false
                onButtonStatusChange(isButtonEnabled) // Deshabilitar el botón tras enviar la alerta
            }
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun showAlertInputDialog(
    latitud: Double,
    longitud: Double,
    uid:String,
    onSend: () -> Unit,
    onDismiss: () -> Unit
) {
    var alertDetails by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(text = "Alerta Recibida!") },
        text = {
            Column {
                Text("Latitud: $latitud, Longitud: $longitud") // Mostrar ubicación
                Text("¿Puedes darnos más detalles sobre la situación?")
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
            Button(onClick = {
                handleSendAlert(latitud, longitud, uid, alertDetails) {
                    onSend()
                }
            }) {
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

@RequiresApi(Build.VERSION_CODES.O)
fun handleSendAlert(
    latitud: Double,
    longitud: Double,
    uid: String,
    alertDetails: String,
    onSuccess: () -> Unit
) {
    val fireStoreServiceAlert = FireStoreServiceAlert()
    val userFireStoreService = UserFireStoreService()
    val alert = Alert().apply {
        latitude = latitud
        longitude = longitud
        detail = alertDetails
        typeAlert = ButtonConfig().title
        date = LocalDateTime.now().toString()
    }

    // Obtener la información del usuario y guardar la alerta
    userFireStoreService.getUserbyUid(uid) { user ->
        if (user != null) {
            alert.user = user
            fireStoreServiceAlert.saveAlertInFireStore(alert)
            onSuccess() // Notifica que la alerta fue enviada
        }
    }
}


@Composable
fun showConfirmationDialog(onAccept: () -> Unit) {
    AlertDialog(
        onDismissRequest = { },
        title = { Text(text = "Información Enviada") },
        text = { Text("Información enviada con éxito, revisa el apartado de notificaciones para tener información sobre el estado de tu alerta." +
                "\nNOTA: No podrás volver a enviar esta misma alerta hasta que atendamos tu caso, esperamos su comprensión.") },
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