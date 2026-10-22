package com.example.codigoch.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ProfileScreen(
    userName: String = "German Pino",
    userEmail: String = "german@gmail.com",
    onNavigateToFavorites: () -> Unit,
    onNavigateToSupport: () -> Unit,
    onLogout: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F9FC))
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Perfil",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1E293B)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Encabezado de Usuario
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF2563EB)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(36.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = userName,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A)
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = userEmail,
                    fontSize = 14.sp,
                    color = Color(0xFF64748B)
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Menú de opciones
        ProfileOptionItem(
            icon = Icons.Default.FavoriteBorder,
            title = "Mis favoritos",
            onClick = onNavigateToFavorites
        )

        Spacer(modifier = Modifier.height(12.dp))

        ProfileOptionItem(
            icon = Icons.Default.Settings,
            title = "Configuración",
            onClick = { }
        )

        Spacer(modifier = Modifier.height(12.dp))

        ProfileOptionItem(
            icon = Icons.Default.Notifications,
            title = "Notificaciones",
            onClick = { }
        )

        Spacer(modifier = Modifier.height(12.dp))

        ProfileOptionItem(
            icon = Icons.AutoMirrored.Filled.Help,
            title = "¿Necesitas ayuda?",
            onClick = onNavigateToSupport
        )

        Spacer(modifier = Modifier.height(12.dp))

        ProfileOptionItem(
            icon = Icons.Default.Info,
            title = "Sobre la app",
            onClick = { }
        )

        Spacer(modifier = Modifier.height(12.dp))

        ProfileOptionItem(
            icon = Icons.AutoMirrored.Filled.ExitToApp,
            title = "Cerrar sesión",
            titleColor = Color(0xFFDC2626),
            onClick = onLogout
        )

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun ProfileOptionItem(
    icon: ImageVector,
    title: String,
    titleColor: Color = Color(0xFF1E293B),
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (titleColor == Color(0xFFDC2626)) Color(0xFFDC2626) else Color(0xFF2563EB),
                modifier = Modifier.size(22.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = title,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = titleColor,
                modifier = Modifier.weight(1f)
            )

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = Color(0xFFCBD5E1)
            )
        }
    }
}
