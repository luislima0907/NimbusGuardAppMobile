package com.crissnm.registrousuarios.Componentes.Inicio

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.crissnm.registrousuarios.Componentes.Notificacion.NotificationService
import com.crissnm.registrousuarios.ManejoDeUsuarios.UserFireStoreService
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun BotonDeAlerta(
    uid: String,
    idAlert: String,
    buttonId: String,
    navController: NavController,
    buttonConfig: ButtonConfig,
    fusedLocationClient: FusedLocationProviderClient,
    permissionsState: MultiplePermissionsState,
    context: Context,
    onButtonStatusChange: (Boolean) -> Unit
) {
    var showAlertDialog by remember { mutableStateOf(false) }
    var showConfirmationDialog by remember { mutableStateOf(false) }
    var latitud by remember { mutableStateOf(0.0) }
    var longitud by remember { mutableStateOf(0.0) }
    var buttonTitle by remember { mutableStateOf("") }
    var timeRemaining by remember { mutableStateOf(0) }
    var isButtonEnabled = remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        val (buttonState, remainingTime) = getButtonState(context, uid, buttonId)
        isButtonEnabled.value = buttonState
        timeRemaining = remainingTime

        if (!isButtonEnabled.value && timeRemaining > 0) {
            startTimer(context, uid, buttonId, timeRemaining, isButtonEnabled) { updatedTimeRemaining ->
                timeRemaining = updatedTimeRemaining
                if (updatedTimeRemaining == 0) {
                    isButtonEnabled.value = true
                }
            }
        }
    }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        permissions.entries.forEach {
            if (!it.value) {
                Toast.makeText(context, "Permiso de ubicación no concedido.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    Button(
        onClick = {
            if (isButtonEnabled.value) {
                if (permissionsState.allPermissionsGranted) {
                    getCurrentLocation(fusedLocationClient) { lat, lon ->
                        latitud = lat
                        longitud = lon
                        buttonTitle = buttonConfig.title
                        showAlertDialog = true
                    }
                } else {
                    requestPermissionLauncher.launch(arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ))
                }
            } else {
                Toast.makeText(context, "Este botón está deshabilitado temporalmente.", Toast.LENGTH_SHORT).show()
            }
        },
        enabled = isButtonEnabled.value,
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

    if (!isButtonEnabled.value && timeRemaining > 0) {
        Text(
            text = "Reactivación en: ${timeRemaining} segundos",
            modifier = Modifier.padding(8.dp)
        )
    }

    if (showAlertDialog) {
        showAlertInputDialog(
            state = "Pendiente",
            latitud = latitud,
            longitud = longitud,
            uid = uid,
            typeAlert = buttonTitle,
            idButton = buttonId,
            idAlert = idAlert,
            onSend = { alertId ->
                showAlertDialog = false
                showConfirmationDialog = true
                isButtonEnabled.value = false
                timeRemaining = 30
                onButtonStatusChange(false)
                saveButtonState(context, uid, buttonId, isButtonEnabled.value, timeRemaining)
                startTimer(context = context, uid = uid, buttonId =  buttonId, initialTime = timeRemaining, isButtonEnabledState = isButtonEnabled) { updatedTimeRemaining ->
                    timeRemaining = updatedTimeRemaining
                }
            },
            onDismiss = { showAlertDialog = false }
        )
    }

    if (showConfirmationDialog) {
        showConfirmationDialog(onAccept = { showConfirmationDialog = false })
    }
}

private fun startTimer(
    context: Context,
    uid: String,
    buttonId: String,
    initialTime: Int,
    isButtonEnabledState: MutableState<Boolean>,
    onTimeUpdate: (Int) -> Unit
) {
    val scope = CoroutineScope(Dispatchers.Main)
    var remainingTime = initialTime

    scope.launch {
        while (remainingTime > 0) {
            delay(1000L)
            remainingTime -= 1
            saveButtonState(context = context, uid = uid, buttonId = buttonId, isEnabled = false, timeRemaining = remainingTime)
            onTimeUpdate(remainingTime)
        }
        saveButtonState(context = context, uid = uid, buttonId = buttonId, isEnabled = true, timeRemaining = 0)
        isButtonEnabledState.value = true
        onTimeUpdate(0)
    }
}

fun saveButtonState(context: Context, uid: String, buttonId: String, isEnabled: Boolean, timeRemaining: Int) {
    val sharedPreferences = context.getSharedPreferences("button_states", Context.MODE_PRIVATE)
    with(sharedPreferences.edit()) {
        putBoolean("$uid $buttonId", isEnabled)
        putInt("$uid ${buttonId}_timeRemaining", timeRemaining)
        apply()
    }
}

fun getButtonState(context: Context, uid: String, buttonId: String): Pair<Boolean, Int> {
    val sharedPreferences = context.getSharedPreferences("button_states", Context.MODE_PRIVATE)
    val isEnabled = sharedPreferences.getBoolean("$uid $buttonId", true)
    val timeRemaining = sharedPreferences.getInt("$uid ${buttonId}_timeRemaining", 30)
    return Pair(isEnabled, timeRemaining)
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun showAlertInputDialog(
    state: String,
    idAlert: String,
    latitud: Double,
    longitud: Double,
    uid: String,
    typeAlert: String,
    idButton: String,
    onSend: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var alertDetails by remember { mutableStateOf("") }

    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(text = "Alerta Recibida!") },
        text = {
            Column {
                Text("Latitud: $latitud, Longitud: $longitud")
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
                handleSendAlert(
                    context =  context,
                    idAlert =  idAlert,
                    state =  state,
                    latitud =  latitud,
                    longitud =  longitud,
                    uid =  uid,
                    alertDetails =  alertDetails,
                    typeAlert =  typeAlert,
                    idButton =  idButton
                ) { alertId ->
                    onSend(alertId)
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
    context: Context,
    idAlert: String,
    state: String,
    latitud: Double,
    longitud: Double,
    uid: String,
    alertDetails: String,
    typeAlert: String,
    idButton: String,
    onSuccess: (String) -> Unit
) {
    val fireStoreServiceAlert = FireStoreServiceAlert()
    val userFireStoreService = UserFireStoreService()
    var alert = Alert()
    alert.apply {
        this.idAlert = ""
        this.state = state
        this.latitude = latitud
        this.longitude = longitud
        this.detail = alertDetails
        this.typeAlert = typeAlert
        this.idButton = idButton
        this.userId = uid
        this.date = LocalDateTime.now().toString()
    }

    userFireStoreService.getUserbyUid(uid) { user ->
        if (user != null) {
            alert.user = user
            fireStoreServiceAlert.saveAlertInFireStore(alert, onSuccess = { alertId ->
                alert.apply {
                    this.idAlert = alertId
                }
                val db = FirebaseFirestore.getInstance()
                db.collection("alerts")
                    .document(alertId)
                    .set(alert)
                val alertService = NotificationService(context)
                alertService.createNotification(
                    alertId =  alertId,
                    estado =  state,
                    message =  alertDetails,
                    typeAlert =  typeAlert,
                    latitud =  latitud.toString(),
                    longitud =  longitud.toString()
                )
                onSuccess(alertId)
            }, onFailure = {
                Log.e("DEBUG", "Error al guardar la alerta")
            })
        } else {
            Log.d("DEBUG", "No se encontró el usuario con UID: $uid")
        }
    }
}

@Composable
fun showConfirmationDialog(onAccept: () -> Unit) {
    AlertDialog(
        onDismissRequest = { },
        title = { Text(text = "Información Enviada") },
        text = { Text("Información enviada con éxito, revisa el apartado de notificaciones para tener información sobre el estado de tu alerta." +
                "\nNOTA: No podrás volver a enviar esta misma alerta hasta que pase al menos 30 segundos, esperamos tu comprension.") },
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
