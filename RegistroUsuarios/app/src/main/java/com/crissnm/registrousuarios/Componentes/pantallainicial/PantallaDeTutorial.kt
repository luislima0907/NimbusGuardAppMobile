package com.crissnm.registrousuarios.Componentes.pantallainicial

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.crissnm.registrousuarios.R


val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

val fontName = GoogleFont("Spicy Rice")
val fontName2 = GoogleFont("Nerko One")

val fontFamily = FontFamily(
    Font(googleFont = fontName, fontProvider = provider),
)
val fontFamily2 = FontFamily(
    Font(googleFont = fontName2, fontProvider = provider)
)

@Composable
fun TextoBienvenida() {
    Text(
        text = "Bienvenido a NimbusGuard",
        style = MaterialTheme.typography.titleLarge,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(bottom = 8.dp),
        color = Color.Black,
        fontSize = 27.sp,
        //fontWeight = FontWeight.Bold,
        fontFamily = fontFamily
    )
}

@Composable
fun InstruccionesUso() {
    Text(
        text = "NimbusGuard es una aplicación que te ayudará a " +
                "reportar todo tipo de emergencias con solo un click.\n" +
                "" +
                "\nRegistrate, si ya tienes una cuenta inicia sesión.",
        style = MaterialTheme.typography.titleMedium,
        textAlign = TextAlign.Left,
        modifier = Modifier.padding(bottom = 8.dp),
        color = Color.Black,
        fontSize = 16.sp,
        fontFamily = fontFamily2
    )
}

@Composable
fun PhotoCarousel() {
    val images = listOf(
        painterResource(id = R.drawable.app0),
        painterResource(id = R.drawable.app1),
        painterResource(id = R.drawable.app2),
        painterResource(id = R.drawable.app3),
        painterResource(id = R.drawable.app4),
    )

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .width(200.dp)
            .height(400.dp)
    ) {
        items(images) { image ->
            Image(
                painter = image,
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(8.dp))
            )
        }
    }
}

@Composable
fun ActionButtons(navController: NavController) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Button(
            onClick = {
                navController.navigate("pantalla_login")
            },
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            ),

            ) {
            Text(
                text = "Iniciar Sesión",
                //fontWeight = FontWeight.Bold,
                fontFamily = fontFamily,
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.width(8.dp))
            Image(
                painter = painterResource(id = R.drawable.inicio),
                contentDescription = "Inicio de sesión",
                modifier = Modifier.size(24.dp)
            )
        }
        Button(
            onClick = {
                navController.navigate("pantalla_registro")
            },
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            )

        ) {
            Text(
                text = "Registrarse  ",
                //fontWeight = FontWeight.Bold,
                fontFamily = fontFamily,
                fontSize = 18.sp
            )
            Image(
                painter = painterResource(id = R.drawable.register),
                contentDescription = "Registro",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
