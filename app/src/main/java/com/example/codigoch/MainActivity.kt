package com.example.codigoch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            // Estado para saber qué pantalla mostrar
            var currentScreen by remember { mutableStateOf("welcome") }

            when (currentScreen) {
                "welcome" -> WelcomeScreen(
                    onNavigateToLogin = { currentScreen = "login" }
                )
                "login" -> LoginScreen()
            }
        }
    }
}