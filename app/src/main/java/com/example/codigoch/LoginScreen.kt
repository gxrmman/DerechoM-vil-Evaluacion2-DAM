package com.example.codigoch

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.codigoch.data.UserDatabaseHelper

val AzulFondoApp = Color(0xFF0F2646)

@Composable
fun LoginScreen(
    dbHelper: UserDatabaseHelper,
    initialIsRegister: Boolean = false,
    onLoginSuccess: (email: String) -> Unit = {},
) {
    var isRegisterMode by remember { mutableStateOf(initialIsRegister) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var errorMessage by remember { mutableStateOf<String?>(null) }
    var successMessage by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AzulFondoApp)
            .padding(24.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = "⚖️ Tus Derechos",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
            )

            Text(
                text = if (isRegisterMode) "Crea una cuenta para acceder" else "Inicia sesión para acceder al Código Civil",
                color = Color.LightGray,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 8.dp, bottom = 24.dp),
            )

            // Selector de pestañas: Iniciar Sesión / Crear Cuenta
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                horizontalArrangement = Arrangement.Center,
            ) {
                FilterChip(
                    selected = !isRegisterMode,
                    onClick = {
                        isRegisterMode = false
                        errorMessage = null
                        successMessage = null
                    },
                    label = { Text("Iniciar Sesión") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color.White,
                        selectedLabelColor = AzulFondoApp,
                        containerColor = Color(0xFF1E3A5F),
                        labelColor = Color.White,
                    ),
                )
                Spacer(modifier = Modifier.width(12.dp))
                FilterChip(
                    selected = isRegisterMode,
                    onClick = {
                        isRegisterMode = true
                        errorMessage = null
                        successMessage = null
                    },
                    label = { Text("Crear Cuenta") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color.White,
                        selectedLabelColor = AzulFondoApp,
                        containerColor = Color(0xFF1E3A5F),
                        labelColor = Color.White,
                    ),
                )
            }

            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    errorMessage = null
                },
                label = { Text("Correo electrónico", color = Color.LightGray) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.Gray,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                ),
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    errorMessage = null
                },
                label = { Text("Contraseña", color = Color.LightGray) },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.Gray,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                ),
                modifier = Modifier.fillMaxWidth(),
            )

            if (isRegisterMode) {
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = {
                        confirmPassword = it
                        errorMessage = null
                    },
                    label = { Text("Confirmar contraseña", color = Color.LightGray) },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.Gray,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                    ),
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            // Mensaje de Error
            errorMessage?.let { msg ->
                Spacer(modifier = Modifier.height(16.dp))
                Surface(
                    color = Color(0xFFEF4444).copy(alpha = 0.2f),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = "⚠️ $msg",
                        color = Color(0xFFFCA5A5),
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(12.dp),
                    )
                }
            }

            // Mensaje de Éxito
            successMessage?.let { msg ->
                Spacer(modifier = Modifier.height(16.dp))
                Surface(
                    color = Color(0xFF10B981).copy(alpha = 0.2f),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = "✅ $msg",
                        color = Color(0xFF6EE7B7),
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(12.dp),
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    val trimmedEmail = email.trim()
                    val trimmedPassword = password.trim()

                    if (isRegisterMode) {
                        if (trimmedEmail.isBlank() || trimmedPassword.isBlank() || confirmPassword.isBlank()) {
                            errorMessage = "Por favor completa todos los campos"
                        } else if (trimmedPassword != confirmPassword.trim()) {
                            errorMessage = "Las contraseñas no coinciden"
                        } else {
                            val registered = dbHelper.registerUser(trimmedEmail, trimmedPassword)
                            if (registered) {
                                dbHelper.login(trimmedEmail, trimmedPassword)
                                successMessage = "¡Cuenta creada exitosamente!"
                                errorMessage = null
                                onLoginSuccess(trimmedEmail)
                            } else {
                                errorMessage = "El usuario ya existe. Por favor inicia sesión."
                            }
                        }
                    } else {
                        if (trimmedEmail.isBlank() || trimmedPassword.isBlank()) {
                            errorMessage = "Ingresa tu correo y contraseña"
                        } else {
                            val loggedIn = dbHelper.login(trimmedEmail, trimmedPassword)
                            if (loggedIn) {
                                errorMessage = null
                                onLoginSuccess(trimmedEmail)
                            } else {
                                errorMessage = "Usuario no encontrado o contraseña incorrecta. Debes crear una cuenta primero."
                            }
                        }
                    }
                },
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = AzulFondoApp,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
            ) {
                Text(
                    text = if (isRegisterMode) "Crear Cuenta" else "Iniciar Sesión",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            TextButton(
                onClick = {
                    isRegisterMode = !isRegisterMode
                    errorMessage = null
                    successMessage = null
                },
            ) {
                Text(
                    text = if (isRegisterMode) "¿Ya tienes cuenta? Inicia sesión" else "¿No tienes cuenta? Créala aquí",
                    color = Color.LightGray,
                    fontSize = 14.sp,
                )
            }
        }
    }
}
