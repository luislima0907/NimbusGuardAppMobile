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
import com.crissnm.registrousuarios.Componentes.Notificacion.AlertService
import com.crissnm.registrousuarios.ManejoDeUsuarios.UserFireStoreService
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.firebase.firestore.FirebaseFirestore
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
    isButtonEnabled: Boolean, // Estado habilitado
    onButtonStatusChange: (Boolean) -> Unit, // Callback para cambiar el estado
    viewModel: BotonDeAlertaViewModel = viewModel(factory = BotonDeAlertaViewModelFactory(LocalContext.current))
) {
    // Estado para el botón
    //var isButtonEnabled by rememberSaveable { mutableStateOf(isButtonEnabled) }

    val enabled = permissionsState.allPermissionsGranted && isButtonEnabled

    //val isButtonEnabled by viewModel.isButtonEnabled.observeAsState(true) // Estado inicial desde el ViewModel

    var isButtonEnabled by rememberSaveable {
        mutableStateOf(getButtonState(context, buttonId)) // Recuperar el estado del botón
    }

//    var isButtonEnabled by rememberSaveable {
//        mutableStateOf(getButtonState(context, uid, buttonId)) // Recuperar el estado del botón
//    }


    // Estado para mostrar los diálogos
    var showAlertDialog by rememberSaveable { mutableStateOf(false) }
    var showConfirmationDialog by rememberSaveable { mutableStateOf(false) }

    // Estado para la ubicación
    var latitud by rememberSaveable { mutableStateOf(0.0) }
    var longitud by rememberSaveable { mutableStateOf(0.0) }
    var buttonTitle by rememberSaveable { mutableStateOf("") }


    // Define un launcher para solicitar permisos
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        permissions.entries.forEach {
            if (!it.value) {
                Toast.makeText(context, "Permiso de ubicación no concedido.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Botón de alerta
    Button(
        onClick = {
            if (isButtonEnabled) {
                if (permissionsState.allPermissionsGranted) {
                    getCurrentLocation(fusedLocationClient) { lat, lon ->
                        latitud = lat
                        longitud = lon
                        buttonTitle = buttonConfig.title
                        //Toast.makeText(context, "Latitud: $lat, Longitud: $lon", Toast.LENGTH_SHORT).show()
                        showAlertDialog = true
                        //onButtonStatusChange(false) // Desactivar el botón
                        //viewModel.disableButton(buttonId) // Deshabilitar el botón
                    }
                } else {
                    // Solicitar los permisos necesarios si no han sido concedidos
                    requestPermissionLauncher.launch(arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ))
                    Toast.makeText(context, "Conceda el permiso de ubicacion.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Este botón está deshabilitado temporalmente.", Toast.LENGTH_SHORT).show()
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
                //Toast.makeText(context, "Alerta enviada con ID: $alertId", Toast.LENGTH_SHORT).show() // Mostrar el ID de la alerta
                // Desactivar el botón después de enviar la alerta
                isButtonEnabled = false
                onButtonStatusChange(false) // Desactivar el botón después de enviar la alerta
            },
            onDismiss = { showAlertDialog = false }
        )
    }

    LaunchedEffect(Unit) {
        // Recupera el estado del botón desde SharedPreferences al inicio
        isButtonEnabled = getButtonState(context, buttonId)
    }

    LaunchedEffect(isButtonEnabled) {
        if (!isButtonEnabled) {
            kotlinx.coroutines.delay(30000) // Espera 30 segundos
            // Reactiva el botón y guarda el estado actualizado
            isButtonEnabled = true
            onButtonStatusChange(true)
            saveButtonState(context, buttonId, isButtonEnabled) // Guarda el nuevo estado
        }
    }


    // Lógica de retardo de 1 minuto
//    LaunchedEffect(isButtonEnabled) {
//        if (!isButtonEnabled) {
//            kotlinx.coroutines.delay(60000) // 60 segundos
//            isButtonEnabled = true // Habilitar de nuevo después de 1 minuto
//            onButtonStatusChange(isButtonEnabled)
//        }
//    }

    // Diálogo de confirmación
    if (showConfirmationDialog) {
        showConfirmationDialog(
            onAccept = {
                showConfirmationDialog = false
                // El botón ya está desactivado después de enviar la alerta, se activará en 1 minuto
            }
        )
    }
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

    val context = LocalContext.current // Obtener el contexto

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
                    context =  context, // Pasa el contexto aquí
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
    context: Context, // Añadir el contexto aquí
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
                    .document(alertId)  // Seleccionamos el documento por ID
                    .set(alert)  // Actualizamos con los nuevos datos
                // Crear instancia de AlertService y enviar notificación
                val alertService = AlertService(context)
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

fun saveButtonState(context: Context, buttonId: String, isEnabled: Boolean) {
    val sharedPreferences = context.getSharedPreferences("ButtonPrefs", Context.MODE_PRIVATE)
    with(sharedPreferences.edit()) {
        putBoolean(buttonId, isEnabled)
        apply() // Guarda el estado
    }
}

//fun saveButtonState(context: Context, uid: String, buttonId: String, isEnabled: Boolean) {
//    val sharedPreferences = context.getSharedPreferences("ButtonPrefs", Context.MODE_PRIVATE)
//    with(sharedPreferences.edit()) {
//        putBoolean("$uid-$buttonId", isEnabled) // Clave única por usuario y botón
//        apply() // Guarda el estado
//    }
//}
//
//fun getButtonState(context: Context, uid: String, buttonId: String): Boolean {
//    val sharedPreferences = context.getSharedPreferences("ButtonPrefs", Context.MODE_PRIVATE)
//    return sharedPreferences.getBoolean("$uid-$buttonId", true) // True es el valor por defecto
//}


fun getButtonState(context: Context, buttonId: String): Boolean {
    val sharedPreferences = context.getSharedPreferences("ButtonPrefs", Context.MODE_PRIVATE)
    return sharedPreferences.getBoolean(buttonId, true) // True es el valor por defecto
}
