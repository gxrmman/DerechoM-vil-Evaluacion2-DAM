package com.example.codigoch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import com.example.codigoch.ui.MainContainer
import com.example.codigoch.ui.theme.CodigoCHTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            CodigoCHTheme {
                // Estado para saber qué pantalla mostrar
                var currentScreen by remember { mutableStateOf("welcome") }
                var userEmail by remember { mutableStateOf("") }

                when (currentScreen) {
                    "welcome" -> WelcomeScreen(
                        onNavigateToLogin = { currentScreen = "login" }
                    )
                    "login" -> LoginScreen(
                        onLoginSuccess = { email ->
                            userEmail = email
                            currentScreen = "main"
                        }
                    )
                    "main" -> MainContainer(
                        userEmail = userEmail,
                        onLogout = { currentScreen = "login" }
                    )
                }
            }
        }
    }
}
