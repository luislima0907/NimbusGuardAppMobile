package com.crissnm.registrousuarios.cuerpo

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.crissnm.registrousuarios.DepYmuni.departamento
import com.crissnm.registrousuarios.DepYmuni.municipio
import com.crissnm.registrousuarios.R
import com.crissnm.registrousuarios.User
import com.crissnm.registrousuarios.Validaciones.Validaciones
import com.crissnm.registrousuarios.ui.theme.RegistroUsuariosTheme

@Composable
fun RegistrationForm() {
    val nombres = remember { mutableStateOf("") }
    val apellidos = remember { mutableStateOf("") }
    val cui = remember { mutableStateOf("") }
    val telefono = remember { mutableStateOf("") }
    val departamento = remember { mutableStateOf("") }
    val municipio = remember { mutableStateOf("") }
    val users = remember { mutableStateListOf<User>() }
    val correo = remember { mutableStateOf("") }
    val contrasena = remember { mutableStateOf("") }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, top = 60.dp, bottom = 10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)

    ){

        Titulo()
        ImagenRegis()
        NombreField(nombres)
        ApellidoField(apellidos)
        CUIField(cui, municipio, departamento)
        TelefonoField(telefono)
        DepartamentoField(departamento)
        MunicipioField(municipio)
        CorreoField(correo)
        ContrasenaField(contrasena)

        Spacer(modifier = Modifier.height(3.dp))

        RegisterUserButton {
            val newUser = User(
                nombres.value,
                apellidos.value,
                cui.value,
                telefono.value,
                departamento.value,
                municipio.value,
                correo.value,
                contrasena.value
            )
            users.add(newUser)
        }
    }
}

@Composable
fun Titulo() {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "REGÍSTRATE",
            modifier = Modifier.padding(bottom = 1.dp),
            color = Color.Cyan,
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
            painter = painterResource(id = R.drawable.registro),
            contentDescription = "Descripción de la imagen",
            modifier = Modifier
                .size(30.dp)
                .offset(y = (-10).dp)

        )
    }
}


@Composable
fun NombreField(nombres: MutableState<String>) {
    val errorMessage = remember { mutableStateOf("") }

    TextField(
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
            .fillMaxWidth(),
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
fun ApellidoField(apellidos: MutableState<String>) {
    val errorMessage = remember { mutableStateOf("") }

    TextField(
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
            .fillMaxWidth(),
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

    TextField(
        value = cui.value,
        onValueChange = { newValue ->
            val cleanInput = newValue.filter { it.isDigit() || it.isWhitespace() }
            cui.value = cleanInput

            if (cleanInput.isNotEmpty()) {
                if (Validaciones.isValidCUI(cleanInput)) {
                    errorMessage.value = ""
                    successMessage.value = "CUI válido"

                    val (muni, depto) = obtenerMunicipioYDepartamento(cleanInput)
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
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        isError = errorMessage.value.isNotEmpty()
    )

    if (errorMessage.value.isNotEmpty()) {
        Text(
            text = errorMessage.value,
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.padding(start = 16.dp, top = 4.dp)
        )
    } else if (successMessage.value.isNotEmpty()) {
        Text(
            text = successMessage.value,
            color = MaterialTheme.colorScheme.outline,
            modifier = Modifier.padding(start = 16.dp, top = 4.dp)
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

        TextField(
            value = telefono.value,
            onValueChange = { input ->

                val cleanInput = input.filter { it.isDigit() }

                val limitedInput = if (cleanInput.length > 8) cleanInput.substring(0, 8) else cleanInput

                val formattedInput = if (limitedInput.length > 4) {
                    "${limitedInput.substring(0, 4)} ${limitedInput.substring(4)}"
                } else {
                    limitedInput
                }

                telefono.value = formattedInput

                errorMessage.value = when {
                    formattedInput.isBlank() -> "El campo de teléfono es obligatorio."
                    !Validaciones.isValidPhone(formattedInput) ->
                        "Número de teléfono inválido, debe tener el formato: 0000 0000."
                    else -> ""
                }
            },
            label = { Text("Teléfono", fontSize = 12.sp) },
            modifier = Modifier
                .fillMaxWidth(),
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


fun obtenerMunicipioYDepartamento(cui: String): Pair<String, String> {
    val cleanCUI = cui.replace("\\s".toRegex(), "")
    val length = cleanCUI.length

    val depto = cleanCUI.substring(9, 11).toIntOrNull() ?: return Pair("", "")
    val muni = if (length == 13) {
        cleanCUI.substring(11, 13).toIntOrNull() ?: return Pair("", "")
    } else {
        cleanCUI.substring(11, 12).toIntOrNull() ?: return Pair("", "")
    }

    val municipio = obtenerNombreMunicipio(depto, muni)
    val departamento = obtenerNombreDepartamento(depto)

    return Pair(municipio, departamento)
}

fun obtenerNombreMunicipio(depto: Int, muni: Int): String {
    return municipio.municipiosMap[depto]?.get(muni) ?: "Desconocido"
}

fun obtenerNombreDepartamento(depto: Int): String {
    return departamento.departamentosMap[depto] ?: "Desconocido"
}

@Composable
fun DepartamentoField(departamento: MutableState<String>) {
    TextField(
        value = departamento.value,
        onValueChange = { departamento.value = it },
        label = { Text("Departamento", fontSize = 12.sp) },
        modifier = Modifier
            .fillMaxWidth(),
        textStyle = TextStyle(fontSize = 14.sp),
        readOnly = true
    )
}

@Composable
fun MunicipioField(municipio: MutableState<String>) {
    TextField(
        value = municipio.value,
        onValueChange = { municipio.value = it },
        label = { Text("Municipio", fontSize = 12.sp) },
        modifier = Modifier
            .fillMaxWidth(),
        textStyle = TextStyle(fontSize = 14.sp),
        readOnly = true
    )
}


@Composable
fun CorreoField(email: MutableState<String>){
    val errorMessage = remember { mutableStateOf("") }

    TextField(
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
            .fillMaxWidth(),
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
fun ContrasenaField(contrasena: MutableState<String>) {
    TextField(
        value = contrasena.value,
        onValueChange = { newValue ->
            contrasena.value = newValue
        },
        label = { Text("Contraseña", fontSize = 12.sp) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 0.dp),
        textStyle = TextStyle(fontSize = 12.sp),
        visualTransformation = PasswordVisualTransformation()
    )
}

@Composable
fun RegisterUserButton(onClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = onClick,
            modifier = Modifier.width(200.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Magenta,
                contentColor = Color.White
            )
        ) {
            Text(
                text = "Registrarme",
                fontWeight = FontWeight.Bold,
            )
        }
        Spacer(modifier = Modifier.height(2.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun RegistrationFormPreview() {
    RegistroUsuariosTheme {
        RegistrationForm()
    }
}
