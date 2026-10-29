package com.example.codigoch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.example.codigoch.data.UserDatabaseHelper
import com.example.codigoch.ui.MainContainer
import com.example.codigoch.ui.theme.CodigoCHTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            CodigoCHTheme {
                val context = LocalContext.current
                val dbHelper = remember { UserDatabaseHelper(context) }

                // Comprobar si hay una sesión activa guardada en la Base de Datos SQLite
                val activeUser = remember { dbHelper.getActiveSessionUser() }

                var currentScreen by remember {
                    mutableStateOf(if (activeUser != null) "main" else "welcome")
                }
                var userEmail by remember {
                    mutableStateOf(activeUser ?: "")
                }

                when (currentScreen) {
                    "welcome" -> WelcomeScreen(
                        onNavigateToLogin = { currentScreen = "login" },
                    )
                    "login" -> LoginScreen(
                        onLoginSuccess = { email, password ->
                            // Guardar usuario y sesión en la Base de Datos SQLite
                            dbHelper.login(email, password)
                            userEmail = email
                            currentScreen = "main"
                        },
                    )
                    "main" -> MainContainer(
                        userEmail = userEmail,
                        onLogout = {
                            // Cerrar sesión en la Base de Datos SQLite
                            dbHelper.logoutAll()
                            userEmail = ""
                            currentScreen = "login"
                        },
                    )
                }
            }
        }
    }
}
