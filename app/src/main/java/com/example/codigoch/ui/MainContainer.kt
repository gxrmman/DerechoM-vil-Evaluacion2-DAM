package com.example.codigoch.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.codigoch.data.SampleData
import com.example.codigoch.model.Derecho

sealed class NavigationTab(val route: String, val title: String, val selectedIcon: ImageVector, val unselectedIcon: ImageVector) {
    object Inicio : NavigationTab("inicio", "Inicio", Icons.Filled.Home, Icons.Outlined.Home)
    object Catalogo : NavigationTab("catalogo", "Catálogo", Icons.AutoMirrored.Filled.List, Icons.AutoMirrored.Outlined.List)
    object Favoritos : NavigationTab("favoritos", "Favoritos", Icons.Filled.Favorite, Icons.Outlined.FavoriteBorder)
    object Perfil : NavigationTab("perfil", "Perfil", Icons.Filled.Person, Icons.Outlined.Person)
}

@Composable
fun MainContainer(
    userEmail: String = "",
    onLogout: () -> Unit
) {
    var selectedTab by remember { mutableStateOf<NavigationTab>(NavigationTab.Inicio) }
    var selectedDerecho by remember { mutableStateOf<Derecho?>(null) }
    var showSupportScreen by remember { mutableStateOf(false) }

    val displayEmail = if (userEmail.isBlank()) "usuario@correo.com" else userEmail
    val displayUserName = userEmail
        .substringBefore("@")
        .split(".", "_", "-")
        .filter { it.isNotBlank() }
        .joinToString(" ") { word ->
            word.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
        }
        .ifEmpty { "Usuario" }

    // Estado global de la lista de derechos
    var derechosList by remember { mutableStateOf(SampleData.derechosIniciales) }

    val favoritosList = remember(derechosList) {
        derechosList.filter { it.isFavorito }
    }

    Scaffold(
        bottomBar = {
            if (selectedDerecho == null && !showSupportScreen) {
                NavigationBar(
                    containerColor = Color.White,
                    tonalElevation = 8.dp
                ) {
                    val tabs = listOf(
                        NavigationTab.Inicio,
                        NavigationTab.Catalogo,
                        NavigationTab.Favoritos,
                        NavigationTab.Perfil
                    )

                    tabs.forEach { tab ->
                        val isSelected = selectedTab == tab
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = {
                                selectedTab = tab
                                selectedDerecho = null
                                showSupportScreen = false
                            },
                            icon = {
                                Icon(
                                    imageVector = if (isSelected) tab.selectedIcon else tab.unselectedIcon,
                                    contentDescription = tab.title
                                )
                            },
                            label = {
                                Text(
                                    text = tab.title,
                                    fontSize = 11.sp
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color(0xFF1E3A8A),
                                selectedTextColor = Color(0xFF1E3A8A),
                                indicatorColor = Color(0xFFDBEAFE),
                                unselectedIconColor = Color(0xFF64748B),
                                unselectedTextColor = Color(0xFF64748B)
                            )
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when {
                showSupportScreen -> {
                    SupportScreen(
                        onBackClick = { showSupportScreen = false }
                    )
                }

                selectedDerecho != null -> {
                    DerechoDetailScreen(
                        derecho = selectedDerecho!!,
                        onBackClick = { selectedDerecho = null },
                        onToggleFavorito = { updated ->
                            derechosList = derechosList.map {
                                if (it.id == updated.id) updated else it
                            }
                            selectedDerecho = updated
                        }
                    )
                }

                else -> {
                    when (selectedTab) {
                        NavigationTab.Inicio -> {
                            HomeScreen(
                                userName = displayUserName,
                                onSelectCategory = { _ ->
                                    selectedTab = NavigationTab.Catalogo
                                },
                                onNavigateToCatalog = {
                                    selectedTab = NavigationTab.Catalogo
                                }
                            )
                        }

                        NavigationTab.Catalogo -> {
                            CatalogScreen(
                                onSelectCategoria = { cat ->
                                    val match = derechosList.firstOrNull { it.categoriaId == cat.id }
                                        ?: derechosList.first()
                                    selectedDerecho = match
                                },
                                onSelectDerecho = { derecho ->
                                    selectedDerecho = derecho
                                }
                            )
                        }

                        NavigationTab.Favoritos -> {
                            FavoritesScreen(
                                favoritos = favoritosList,
                                onSelectDerecho = { derecho ->
                                    selectedDerecho = derecho
                                }
                            )
                        }

                        NavigationTab.Perfil -> {
                            ProfileScreen(
                                userName = "German Pino",
                                userEmail = "german@gmail.com",
                                onNavigateToFavorites = {
                                    selectedTab = NavigationTab.Favoritos
                                },
                                onNavigateToSupport = {
                                    showSupportScreen = true
                                },
                                onLogout = onLogout
                            )
                        }
                    }
                }
            }
        }
    }
}
