package com.crissnm.registrousuarios.PantallasDeLaApp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.crissnm.registrousuarios.Componentes.Notificacion.AlertService
import com.crissnm.registrousuarios.Componentes.Notificacion.Notificacion
import com.crissnm.registrousuarios.Componentes.Notificacion.NotificacionRepository
import com.crissnm.registrousuarios.Componentes.pantallainicial.fontFamily
import com.google.firebase.auth.FirebaseAuth

@Composable
fun PantallaDeNotificacion(
    navController: NavController
) {
    val context = LocalContext.current
    val alertService = AlertService(context)

    LaunchedEffect(Unit) {
        alertService.listenChangesOnAlerts()
    }
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    val notificaciones = NotificacionRepository.obtenerNotificaciones(userId)
    contenidoPantallaDeNotificacion(navController, notificaciones)
}



@Composable
fun contenidoPantallaDeNotificacion(
    navController: NavController,
    notificaciones: List<Notificacion>
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(130.dp))
            Text(
                text = "Notificaciones del Estado de tus Alertas",
                style = MaterialTheme.typography.titleLarge,
                fontFamily = fontFamily
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(notificaciones) { notificacion ->
                    NotificacionItem(notificacion)
                }
            }
        }
    }
}


@Composable
fun NotificacionItem(notificacion: Notificacion) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Alerta ID: ${notificacion.alertId}", fontWeight = FontWeight.Bold)
            Text(text = "Estado: ${notificacion.estado}")
            Text(text = "Detalles: ${notificacion.message}")
            Text(text = "Tipo de Alerta: ${notificacion.alertType}")
            Text(text = "Latitud: ${notificacion.latitud}")
            Text(text = "Longitud: ${notificacion.longitud}")
        }
    }
}
