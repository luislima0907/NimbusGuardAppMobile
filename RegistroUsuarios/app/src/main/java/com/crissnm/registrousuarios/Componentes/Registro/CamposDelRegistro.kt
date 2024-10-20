package com.crissnm.registrousuarios.Componentes.Registro

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.crissnm.registrousuarios.DepYmuni.ValidarCUI
import com.crissnm.registrousuarios.ManejoDeUsuarios.Validaciones
import com.crissnm.registrousuarios.R


@Composable
fun Titulo() {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "REGÍSTRATE",
            modifier = Modifier.padding(bottom = 0.dp),
            color = Color.Black,
            fontSize = 27.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Serif
        )
    }
}

@Composable
fun ImagenRegis(){
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.registrouser),
            contentDescription = "Registrate",
            modifier = Modifier
                .size(35.dp)
                .offset(y = (-10).dp)

        )
    }
}


@Composable
fun NombreField(nombres: MutableState<String>) {
    val errorMessage = remember { mutableStateOf("") }

    OutlinedTextField(
        value = nombres.value,
        onValueChange = {
            nombres.value = it

            errorMessage.value = when {
                it.isBlank() -> "El campo de nombres es obligatorio."
                !Validaciones.isValidName(it) -> "El nombre debe comenzar con una letra mayúscula y solo contener letras, no admite otros caracteres."
                else -> ""
            }
        },
        label = { Text("Nombres", fontSize = 12.sp) },
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(bottom = 1.dp),
        textStyle = TextStyle(fontSize = 14.sp),
        isError = errorMessage.value.isNotEmpty(),
        singleLine = true,

    )

    if (errorMessage.value.isNotEmpty()) {
        Text(
            text = errorMessage.value,
            color = MaterialTheme.colorScheme.error,
            fontSize = 12.sp,
            modifier = Modifier.padding(start = 16.dp, top = 2.dp)
        )
    }
}

@Composable
fun ApellidoField(apellidos: MutableState<String>) {
    val errorMessage = remember { mutableStateOf("") }

    OutlinedTextField(
        value = apellidos.value,
        onValueChange = {
            apellidos.value = it

            errorMessage.value = when {
                it.isBlank() -> "El campo de apellidos es obligatorio."
                !Validaciones.isValidName(it) -> "El apellido debe comenzar con una letra mayúscula y solo contener letras, no admite otros caracteres."
                else -> ""
            }
        },
        label = { Text("Apellidos", fontSize = 12.sp) },
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(bottom = 1.dp),
        textStyle = TextStyle(fontSize = 14.sp),
        isError = errorMessage.value.isNotEmpty()
    )

    if (errorMessage.value.isNotEmpty()) {
        Text(
            text = errorMessage.value,
            color = MaterialTheme.colorScheme.error,
            fontSize = 12.sp,
            modifier = Modifier.padding(start = 16.dp, top = 2.dp)
        )
    }
}

@Composable
fun CUIField(cui: MutableState<String>, municipio: MutableState<String>, departamento: MutableState<String>) {
    val errorMessage = remember { mutableStateOf("") }
    val successMessage = remember { mutableStateOf("") }

    OutlinedTextField(
        value = cui.value,
        onValueChange = { newValue ->
            val cleanInput = newValue.filter { it.isDigit() || it.isWhitespace() }
            cui.value = cleanInput

            if (cleanInput.isNotEmpty()) {
                if (Validaciones.isValidCUI(cleanInput)) {
                    errorMessage.value = ""
                    successMessage.value = "CUI válido"


                    val infoMunicipioYDepartamento: ValidarCUI
                    infoMunicipioYDepartamento = ValidarCUI()
                    val (muni, depto) = infoMunicipioYDepartamento.obtenerMunicipioYDepartamento(cleanInput)
                    municipio.value = muni
                    departamento.value = depto
                } else {
                    errorMessage.value = "Formato incorrecto o código inválido"
                    successMessage.value = ""
                    municipio.value = ""
                    departamento.value = ""
                }
            } else {
                errorMessage.value = "El campo de CUI es obligatorio."
                successMessage.value = ""
                municipio.value = ""
                departamento.value = ""
            }
        },
        label = { Text("CUI", fontSize = 12.sp) },
        modifier = Modifier
            .fillMaxWidth()
            .height(62.dp),
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        isError = errorMessage.value.isNotEmpty()
    )

    if (errorMessage.value.isNotEmpty()) {
        Text(
            text = errorMessage.value,
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.padding(start = 16.dp, top = 2.dp)
        )
    } else if (successMessage.value.isNotEmpty()) {
        Text(
            text = successMessage.value,
            color = MaterialTheme.colorScheme.outline,
            fontSize = 12.sp,
            modifier = Modifier.padding(start = 16.dp, top = 2.dp)
        )
    }
}

@Composable
fun TelefonoField(telefono: MutableState<String>) {
    val errorMessage = remember { mutableStateOf("") }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Texto fijo para prefijo +502
        Text(
            text = "+502", fontSize = 12.sp,
            modifier = Modifier
                .width(60.dp)
                .padding(top = 0.dp, bottom = 0.dp)
        )

        OutlinedTextField(
            value = telefono.value,
            onValueChange = { input ->
                // Filtrar solo dígitos
                val cleanInput = input.filter { it.isDigit() }

                // Limitar a 8 dígitos
                val limitedInput = if (cleanInput.length > 8) cleanInput.substring(0, 8) else cleanInput

                // Formatear solo si se han ingresado los 8 dígitos completos
                val formattedInput = if (limitedInput.length == 8) {
                    "${limitedInput.substring(0, 4)} ${limitedInput.substring(4)}"
                } else {
                    limitedInput
                }

                telefono.value = formattedInput

                // Validar el formato del teléfono
                errorMessage.value = when {
                    formattedInput.isBlank() -> "El campo de teléfono es obligatorio."
                    !Validaciones.isValidPhone(formattedInput) ->
                        "Número de teléfono inválido, debe tener el formato: 0000 0000."
                    else -> ""
                }
            },
            label = { Text("Teléfono", fontSize = 12.sp) },
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(vertical = 0.dp),
            textStyle = TextStyle(fontSize = 14.sp),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            isError = errorMessage.value.isNotEmpty()
        )
    }

    if (errorMessage.value.isNotEmpty()) {
        Text(
            text = errorMessage.value,
            color = MaterialTheme.colorScheme.error,
            fontSize = 12.sp,
            modifier = Modifier.padding(start = 16.dp, top = 2.dp)
        )
    }
}



@Composable
fun DepartamentoField(departamento: MutableState<String>) {
    OutlinedTextField(
        value = departamento.value,
        onValueChange = { departamento.value = it },
        label = { Text("Departamento", fontSize = 12.sp) },
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        textStyle = TextStyle(fontSize = 14.sp),
        readOnly = true
    )
}

@Composable
fun MunicipioField(municipio: MutableState<String>) {
    OutlinedTextField(
        value = municipio.value,
        onValueChange = { municipio.value = it },
        label = { Text("Municipio", fontSize = 12.sp) },
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        textStyle = TextStyle(fontSize = 14.sp),
        readOnly = true
    )
}

@Composable
fun CorreoField(email: MutableState<String>){
    val errorMessage = remember { mutableStateOf("") }

    OutlinedTextField(
        value = email.value,
        onValueChange = { newValue ->
            email.value = newValue

            errorMessage.value = when {
                newValue.isBlank() -> "El campo de correo es obligatorio."
                !Validaciones.isValidCorreo(newValue) -> "Correo electrónico inválido."
                else -> ""
            }
        },
        label = { Text("Correo", fontSize = 12.sp) },
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(top = 0.dp, bottom = 0.dp),
        textStyle = TextStyle(fontSize = 14.sp),
        isError = errorMessage.value.isNotEmpty(),
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email)
    )

    if (errorMessage.value.isNotEmpty()) {
        Text(
            text = errorMessage.value,
            color = MaterialTheme.colorScheme.error,
            fontSize = 12.sp,
            modifier = Modifier.padding(start = 16.dp, top = 2.dp)
        )
    }
}

@Composable
fun ContrasenaField(contrasena: MutableState<String>, errorMessage: MutableState<String>) {
    OutlinedTextField(
        value = contrasena.value,
        onValueChange = { newValue ->
            contrasena.value = newValue
            // Validar longitud de la contraseña
            if (newValue.length < 6) {
                errorMessage.value = "La contraseña debe tener al menos 6 caracteres."
            } else {
                errorMessage.value = ""
            }
        },
        label = { Text("Contraseña", fontSize = 12.sp) },
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(vertical = 0.dp),
        textStyle = TextStyle(fontSize = 12.sp),
        visualTransformation = PasswordVisualTransformation(),
        isError = errorMessage.value.isNotEmpty()  // Activar estado de error
    )

    // Mostrar el mensaje de error si existe
    if (errorMessage.value.isNotEmpty()) {
        Text(
            text = errorMessage.value,
            color = Color.Red,  // Color del mensaje de error
            fontSize = 12.sp,
            modifier = Modifier.padding(start = 16.dp)  // Ajusta el margen según sea necesario
        )
    }
}

@Composable
fun RegisterUserButton(
    nombres: MutableState<String>,
    apellidos: MutableState<String>,
    cui: MutableState<String>,
    telefono: MutableState<String>,
    departamento: MutableState<String>,
    municipio: MutableState<String>,
    email: MutableState<String>,
    contrasena: MutableState<String>,
    onClick: () -> Unit
) {
    val context = LocalContext.current
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {
                ValidarCampos(nombres, apellidos, cui, telefono, departamento, municipio, email, contrasena, context)
                onClick()
            },
            modifier = Modifier.width(200.dp)
            .height(55.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Blue,
                contentColor = Color.White
            )
        ) {
            Text(
                text = "Registrarme",
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif,
                fontSize = 16.sp

            )
        }
        Spacer(modifier = Modifier.height(1.dp))
    }
}


fun ValidarCampos(
    nombres: MutableState<String>,
    apellidos: MutableState<String>,
    cui: MutableState<String>,
    telefono: MutableState<String>,
    departamento: MutableState<String>,
    municipio: MutableState<String>,
    email: MutableState<String>,
    contrasena: MutableState<String>,
    context: Context
){
            // Validar nombre
            if (!Validaciones.isValidName(nombres.value) || nombres.value.isBlank()) {
                Toast.makeText(context, "Nombre inválido", Toast.LENGTH_SHORT).show()
            }

    if (!Validaciones.isValidName(apellidos.value) || apellidos.value.isBlank()) {
        Toast.makeText(context, "Apellido inválido", Toast.LENGTH_SHORT).show()
    }
            // Validar teléfono
            if (!Validaciones.isValidPhone(telefono.value) || telefono.value.isBlank()) {
                Toast.makeText(context, "Teléfono inválido (Formato: 1234 5678)", Toast.LENGTH_SHORT).show()
            }

            // Validar correo
            if (!Validaciones.isValidCorreo(email.value) || email.value.isBlank()) {
                Toast.makeText(context, "Correo inválido", Toast.LENGTH_SHORT).show()
            }

    if (contrasena.value.isBlank()) {
        Toast.makeText(context, "Contraseña inválida", Toast.LENGTH_SHORT).show()
    }

            // Validar CUI
            if (!Validaciones.isValidCUI(cui.value) || cui.value.isBlank()) {
                Toast.makeText(context, "CUI inválido", Toast.LENGTH_SHORT).show()
            }

    if (departamento.value.isBlank() || municipio.value.isBlank()) {
        Toast.makeText(context, "Departamento o Municipio inválido", Toast.LENGTH_SHORT).show()
    }
}
