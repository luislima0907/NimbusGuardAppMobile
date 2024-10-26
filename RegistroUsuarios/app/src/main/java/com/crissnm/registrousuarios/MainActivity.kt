package com.crissnm.registrousuarios

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.crissnm.registrousuarios.Navegacion.navegacionDeLaApp
import com.crissnm.registrousuarios.ui.theme.RegistroUsuariosTheme
import com.google.firebase.FirebaseApp


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        enableEdgeToEdge()
        setContent {
            RegistroUsuariosTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = androidx.compose.material3.MaterialTheme.colorScheme.background
                ) {
                    navegacionDeLaApp()
                }
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun RegistrationFormPreview() {
//    RegistroUsuariosTheme {
//        Surface(
//            modifier = Modifier.fillMaxSize(),
//            color = androidx.compose.material3.MaterialTheme.colorScheme.background
//        ) {
//            navegacionDeLaApp()
//        }
//    }
//}