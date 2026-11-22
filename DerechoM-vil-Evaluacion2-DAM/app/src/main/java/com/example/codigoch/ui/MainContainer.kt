
package com.example.codigoch.ui

import android.graphics.Bitmap
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.codigoch.data.repository.DerechoRepository
import com.example.codigoch.data.repository.UserRepository
import com.example.codigoch.model.Derecho
import com.example.codigoch.viewmodel.ConsultasViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

sealed class NavigationTab(val route: String, val title: String, val selectedIcon: ImageVector, val unselectedIcon: ImageVector) {
    object Inicio : NavigationTab("inicio", "Inicio", Icons.Filled.Home, Icons.Outlined.Home)
    object Catalogo : NavigationTab("catalogo", "Catálogo", Icons.AutoMirrored.Filled.List, Icons.AutoMirrored.Outlined.List)
    object Mapa : NavigationTab("mapa", "Mapa", Icons.Filled.Map, Icons.Outlined.Map)
    object Favoritos : NavigationTab("favoritos", "Favoritos", Icons.Filled.Favorite, Icons.Outlined.FavoriteBorder)
    object Perfil : NavigationTab("perfil", "Perfil", Icons.Filled.Person, Icons.Outlined.Person)
}

@Composable
fun MainContainer(
    userEmail: String = "",
    userRepository: UserRepository,
    derechoRepository: DerechoRepository,
    onLogout: () -> Unit
) {
    var selectedTab by remember { mutableStateOf<NavigationTab>(NavigationTab.Inicio) }
    var selectedDerecho by remember { mutableStateOf<Derecho?>(null) }
    var showSupportScreen by remember { mutableStateOf(false) }
    var showConsultasScreen by remember { mutableStateOf(false) }
    val consultasViewModel: ConsultasViewModel = viewModel(factory = ConsultasViewModel.Factory)

    val displayEmail = userEmail.ifBlank { "usuario@correo.com" }
    val displayUserName = userEmail
        .substringBefore("@")
        .split(".", "_", "-")
        .filter { it.isNotBlank() }
        .joinToString(" ") { word ->
            word.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
        }
        .ifEmpty { "Usuario" }

    // Estado local reactivo de la lista de derechos gestionada mediante el repositorio
    var derechosList by remember { mutableStateOf(derechoRepository.getDerechos()) }

    // Estado reactivo de foto de perfil
    var profilePhotoState by remember { mutableStateOf<Bitmap?>(userRepository.getProfilePhoto(displayEmail)) }

    val favoritosList = remember(derechosList) {
        derechoRepository.getFavoritos()
    }

    Scaffold(
        bottomBar = {
            if (selectedDerecho == null && !showSupportScreen && !showConsultasScreen) {
                NavigationBar(
                    containerColor = Color.White,
                    tonalElevation = 8.dp
                ) {
                    val tabs = listOf(
                        NavigationTab.Inicio,
                        NavigationTab.Catalogo,
                        NavigationTab.Mapa,
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
                showConsultasScreen -> {
                    ConsultasScreen(
                        viewModel = consultasViewModel,
                        onBackClick = { showConsultasScreen = false }
                    )
                }

                showSupportScreen -> {
                    SupportScreen(
                        onBackClick = { showSupportScreen = false },
                        onNavigateToConsultas = { showConsultasScreen = true },
                        onNavigateToMap = {
                            showSupportScreen = false
                            selectedTab = NavigationTab.Mapa
                        }
                    )
                }

                selectedDerecho != null -> {
                    val currentDerecho = selectedDerecho!!
                    val currentNotePhoto = remember(currentDerecho.id) {
                        derechoRepository.getNotePhoto(currentDerecho.id)
                    }

                    DerechoDetailScreen(
                        derecho = currentDerecho,
                        notePhoto = currentNotePhoto,
                        onSaveNotePhoto = { bitmap ->
                            if (bitmap != null) {
                                derechoRepository.saveNotePhoto(currentDerecho.id, bitmap)
                            }
                        },
                        onBackClick = { selectedDerecho = null },
                        onToggleFavorito = { _ ->
                            val updated = derechoRepository.toggleFavorito(currentDerecho.id)
                            if (updated != null) {
                                selectedDerecho = updated
                                derechosList = derechoRepository.getDerechos()
                            }
                        }
                    )
                }

                else -> {
                    when (selectedTab) {
                        NavigationTab.Inicio -> {
                            HomeScreen(
                                userName = displayUserName,
                                derechoRepository = derechoRepository,
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
                                derechoRepository = derechoRepository,
                                onSelectCategoria = { cat ->
                                    val match = derechoRepository.getDerechosByCategoria(cat.id).firstOrNull()
                                        ?: derechoRepository.getDerechos().first()
                                    selectedDerecho = match
                                },
                                onSelectDerecho = { derecho ->
                                    selectedDerecho = derecho
                                }
                            )
                        }

                        NavigationTab.Mapa -> {
                            MapScreen(
                                onBackClick = {
                                    selectedTab = NavigationTab.Inicio
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
                                userName = displayUserName,
                                userEmail = displayEmail,
                                profilePhoto = profilePhotoState,
                                onSaveProfilePhoto = { bitmap ->
                                    userRepository.saveProfilePhoto(displayEmail, bitmap)
                                    profilePhotoState = bitmap
                                },
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
