package com.example.codigoch.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

data class PuntoAtencion(
    val id: String,
    val nombre: String,
    val tipo: String,
    val direccion: String,
    val telefono: String,
    val latitud: Double,
    val longitud: Double,
    val horario: String = "Lunes a Viernes 09:00 - 17:30"
)

val puntosAtencionLegal = listOf(
    PuntoAtencion(
        id = "1",
        nombre = "Corporación de Asistencia Judicial (CAJ)",
        tipo = "Asistencia Legal Gratuita",
        direccion = "Agustinas 1420, Santiago Centro",
        telefono = "+56 2 2688 0000",
        latitud = -33.4419,
        longitud = -70.6542
    ),
    PuntoAtencion(
        id = "2",
        nombre = "Instituto Nacional de Derechos Humanos (INDH)",
        tipo = "Protección de DDHH",
        direccion = "Moneda 1040, Santiago Centro",
        telefono = "+56 2 2887 8800",
        latitud = -33.4372,
        longitud = -70.6506
    ),
    PuntoAtencion(
        id = "3",
        nombre = "Defensoría de la Niñez",
        tipo = "Protección Infanto-Juvenil",
        direccion = "Huérfanos 1294, Piso 7, Santiago",
        telefono = "+56 2 2490 2000",
        latitud = -33.4350,
        longitud = -70.6420
    ),
    PuntoAtencion(
        id = "4",
        nombre = "Inspección del Trabajo",
        tipo = "Derechos Laborales",
        direccion = "Agustinas 1235, Santiago Centro",
        telefono = "600 450 4000",
        latitud = -33.4445,
        longitud = -70.6511
    ),
    PuntoAtencion(
        id = "5",
        nombre = "Tribunales de Familia de Santiago",
        tipo = "Orientación Familiar y Civil",
        direccion = "San Antonio 477, Santiago",
        telefono = "+56 2 2700 0000",
        latitud = -33.4402,
        longitud = -70.6580
    )
)

@Composable
fun MapScreen(
    onBackClick: () -> Unit = {}
) {
    val context = LocalContext.current
    var selectedPunto by remember { mutableStateOf<PuntoAtencion?>(puntosAtencionLegal.first()) }

    // Posición inicial del mapa (Centro de Santiago)
    val santiagoPos = LatLng(-33.4419, -70.6542)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(santiagoPos, 13.5f)
    }

    // Efecto para enfocar la cámara en el punto seleccionado
    LaunchedEffect(selectedPunto) {
        selectedPunto?.let { punto ->
            cameraPositionState.position = CameraPosition.fromLatLngZoom(
                LatLng(punto.latitud, punto.longitud),
                15f
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F9FC))
    ) {
        // Top bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 12.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver",
                    tint = Color(0xFF1E293B)
                )
            }
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                imageVector = Icons.Default.Map,
                contentDescription = null,
                tint = Color(0xFF1E3A8A),
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = "Mapa de Asistencia Legal",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E293B)
                )
                Text(
                    text = "Puntos de orientación y defensorías",
                    fontSize = 12.sp,
                    color = Color(0xFF64748B)
                )
            }
        }

        Box(modifier = Modifier.weight(1f)) {
            // Mapa interactivo de Google Maps
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                uiSettings = MapUiSettings(
                    zoomControlsEnabled = true,
                    compassEnabled = true,
                    myLocationButtonEnabled = false
                )
            ) {
                puntosAtencionLegal.forEach { punto ->
                    Marker(
                        state = rememberMarkerState(position = LatLng(punto.latitud, punto.longitud)),
                        title = punto.nombre,
                        snippet = punto.tipo,
                        onClick = {
                            selectedPunto = punto
                            false
                        }
                    )
                }
            }

            // Chips selector de Puntos en la parte superior del mapa
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp, horizontal = 8.dp)
                    .align(Alignment.TopCenter),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(puntosAtencionLegal) { punto ->
                    val isSelected = selectedPunto?.id == punto.id
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedPunto = punto },
                        label = {
                            Text(
                                text = punto.nombre.take(22) + if (punto.nombre.length > 22) "..." else "",
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = Color.White,
                            labelColor = Color(0xFF1E293B),
                            selectedContainerColor = Color(0xFF1E3A8A),
                            selectedLabelColor = Color.White
                        ),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = if (isSelected) Color.White else Color(0xFF1E3A8A)
                            )
                        }
                    )
                }
            }

            // Card flotante inferior con información detallada del punto seleccionado
            selectedPunto?.let { punto ->
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0xFFDBEAFE)
                            ) {
                                Text(
                                    text = punto.tipo,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1E40AF),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = punto.nombre,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = Color(0xFF64748B),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = punto.direccion,
                                fontSize = 13.sp,
                                color = Color(0xFF475569)
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Call,
                                contentDescription = null,
                                tint = Color(0xFF64748B),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = punto.telefono,
                                fontSize = 13.sp,
                                color = Color(0xFF475569)
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Button(
                                onClick = {
                                    val gmmIntentUri = Uri.parse("geo:${punto.latitud},${punto.longitud}?q=${Uri.encode(punto.nombre)}")
                                    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri).apply {
                                        setPackage("com.google.android.apps.maps")
                                    }
                                    try {
                                        context.startActivity(mapIntent)
                                    } catch (e: Exception) {
                                        val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/search/?api=1&query=${punto.latitud},${punto.longitud}"))
                                        context.startActivity(webIntent)
                                    }
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E3A8A)),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Directions,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = "Cómo llegar", fontSize = 13.sp)
                            }

                            OutlinedButton(
                                onClick = {
                                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${punto.telefono.replace(" ", "")}"))
                                    context.startActivity(intent)
                                },
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Call,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = "Llamar", fontSize = 13.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}
