package com.example.codigoch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.example.codigoch.data.UserDatabaseHelper
import com.example.codigoch.data.repository.DerechoRepositoryImpl
import com.example.codigoch.data.repository.UserRepositoryImpl
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
                val userRepository = remember { UserRepositoryImpl(dbHelper) }
                val derechoRepository = remember { DerechoRepositoryImpl() }

                // Comprobar si hay una sesión activa guardada a través del UserRepository
                val activeUser = remember { userRepository.getActiveSessionUser() }

                var currentScreen by remember {
                    mutableStateOf(if (activeUser != null) "main" else "welcome")
                }
                var initialIsRegister by remember { mutableStateOf(value = false) }
                var userEmail by remember {
                    mutableStateOf(activeUser ?: "")
                }

                when (currentScreen) {
                    "welcome" -> WelcomeScreen(
                        onNavigateToLogin = {
                            initialIsRegister = false
                            currentScreen = "login"
                        },
                        onNavigateToRegister = {
                            initialIsRegister = true
                            currentScreen = "login"
                        },
                    )
                    "login" -> LoginScreen(
                        userRepository = userRepository,
                        initialIsRegister = initialIsRegister,
                        onLoginSuccess = { email ->
                            userEmail = email
                            currentScreen = "main"
                        },
                    )
                    "main" -> MainContainer(
                        userEmail = userEmail,
                        userRepository = userRepository,
                        derechoRepository = derechoRepository,
                        onLogout = {
                            // Cerrar sesión mediante UserRepository
                            userRepository.logoutAll()
                            userEmail = ""
                            initialIsRegister = false
                            currentScreen = "login"
                        },
                    )
                }
            }
        }
    }
}
