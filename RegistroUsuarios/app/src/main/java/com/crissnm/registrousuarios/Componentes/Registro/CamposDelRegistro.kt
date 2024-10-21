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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
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
        onValueChange = { newValue ->
            validarNombre(newValue, nombres, errorMessage)
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

fun validarNombre(input: String, nombres: MutableState<String>, errorMessage: MutableState<String>) {
    nombres.value = input
    errorMessage.value = when {
        input.isBlank() -> "El campo de nombres es obligatorio."
        !Validaciones.isValidName(input) -> "El nombre debe comenzar con una letra mayúscula y solo contener letras, no admite otros caracteres."
        else -> ""
    }
}

@Composable
fun ApellidoField(apellidos: MutableState<String>) {
    val errorMessage = remember { mutableStateOf("") }

    OutlinedTextField(
        value = apellidos.value,
        onValueChange = { newValue ->
            validarApellido(newValue, apellidos, errorMessage)
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

fun validarApellido(input: String, apellidos: MutableState<String>, errorMessage: MutableState<String>) {
    apellidos.value = input
    errorMessage.value = when {
        input.isBlank() -> "El campo de apellidos es obligatorio."
        !Validaciones.isValidName(input) -> "El apellido debe comenzar con una letra mayúscula y solo contener letras, no admite otros caracteres."
        else -> ""
    }
}

@Composable
fun CUIField(cui: MutableState<String>, municipio: MutableState<String>, departamento: MutableState<String>) {
    val errorMessage = remember { mutableStateOf("") }
    val successMessage = remember { mutableStateOf("") }

    OutlinedTextField(
        value = cui.value,
        onValueChange = { newValue ->
            validarCUI(newValue, cui, municipio, departamento, errorMessage, successMessage)
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

fun validarCUI(
    input: String,
    cui: MutableState<String>,
    municipio: MutableState<String>,
    departamento: MutableState<String>,
    errorMessage: MutableState<String>,
    successMessage: MutableState<String>
) {
    val cleanInput = input.filter { it.isDigit() || it.isWhitespace() }
    cui.value = cleanInput

    if (cleanInput.isNotEmpty()) {
        if (Validaciones.isValidCUI(cleanInput)) {
            errorMessage.value = ""
            successMessage.value = "CUI válido"

            val infoMunicipioYDepartamento = ValidarCUI()
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
            onValueChange = { newValue ->
                validarTelefono(newValue, telefono, errorMessage)
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

fun validarTelefono(input: String, telefono: MutableState<String>, errorMessage: MutableState<String>) {
    val cleanInput = input.filter { it.isDigit() }
    val limitedInput = if (cleanInput.length > 8) cleanInput.substring(0, 8) else cleanInput

    val formattedInput = if (limitedInput.length == 8) {
        "${limitedInput.substring(0, 4)} ${limitedInput.substring(4)}"
    } else {
        limitedInput
    }

    telefono.value = formattedInput

    errorMessage.value = when {
        formattedInput.isBlank() -> "El campo de teléfono es obligatorio."
        !Validaciones.isValidPhone(formattedInput) -> "Número de teléfono inválido, debe tener el formato: 0000 0000."
        else -> ""
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
            validarCorreo(newValue, email, errorMessage)
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

fun validarCorreo(input: String, correo: MutableState<String>, errorMessage: MutableState<String>) {
    correo.value = input
    errorMessage.value = when {
        input.isBlank() -> "El campo de correo es obligatorio."
        !Validaciones.isValidCorreo(input) -> "Correo electrónico inválido."
        else -> ""
    }
}

@Composable
fun ContrasenaField(contrasena: MutableState<String>) {
    OutlinedTextField(
        value = contrasena.value,  // Usar el valor del estado
        onValueChange = {
            contrasena.value = it
        },
        label = { Text("Contraseña", fontSize = 12.sp) },
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(vertical = 0.dp),
        textStyle = TextStyle(fontSize = 12.sp),
        visualTransformation = PasswordVisualTransformation()  // Ocultar texto
    )
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

    // Estado de los mensajes de error
    val nombreError = remember { mutableStateOf("") }
    val apellidoError = remember { mutableStateOf("") }
    val cuiError = remember { mutableStateOf("") }
    val telefonoError = remember { mutableStateOf("") }
    val emailError = remember { mutableStateOf("") }

    // Verificar si todos los campos son válidos
    val isFormValid = esFormularioValido(
        nombres.value,
        apellidos.value,
        cui.value,
        telefono.value,
        email.value,
        contrasena.value,
        nombreError,
        apellidoError,
        cuiError,
        telefonoError,
        emailError,
        municipio,
        departamento
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {
                if (isFormValid) {
                    onClick()  // Solo permite continuar si el formulario es válido
                } else {
                    Toast.makeText(context, "Revisa los campos con errores", Toast.LENGTH_LONG).show()
                }
            },
            modifier = Modifier
                .width(200.dp)
                .height(55.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Blue,
                contentColor = Color.White
            ),
            enabled = isFormValid  // Habilitar/deshabilitar el botón
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

fun esFormularioValido(
    nombres: String,
    apellidos: String,
    cui: String,
    telefono: String,
    email: String,
    contrasena: String,
    nombreError: MutableState<String>,
    apellidoError: MutableState<String>,
    cuiError: MutableState<String>,
    telefonoError: MutableState<String>,
    emailError: MutableState<String>,
    municipio: MutableState<String>,
    departamento: MutableState<String>
): Boolean {
    validarNombre(nombres, mutableStateOf(nombres), nombreError)
    validarApellido(apellidos, mutableStateOf(apellidos), apellidoError)
    validarCUI(cui, mutableStateOf(cui), municipio, departamento, cuiError, mutableStateOf(""))
    validarTelefono(telefono, mutableStateOf(telefono), telefonoError)
    validarCorreo(email, mutableStateOf(email), emailError)

    return nombreError.value.isEmpty() &&
            apellidoError.value.isEmpty() &&
            cuiError.value.isEmpty() &&
            telefonoError.value.isEmpty() &&
            emailError.value.isEmpty() &&
            nombres.isNotBlank() &&
            apellidos.isNotBlank() &&
            cui.isNotBlank() &&
            telefono.isNotBlank() &&
            email.isNotBlank() &&
            contrasena.isNotBlank()
}
