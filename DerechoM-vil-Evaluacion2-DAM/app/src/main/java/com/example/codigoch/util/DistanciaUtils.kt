package com.example.codigoch.util

import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

private const val RADIO_TIERRA_METROS = 6_371_000.0

/** Distancia en metros entre dos coordenadas (fórmula de Haversine). */
fun calcularDistanciaMetros(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
    val dLat = Math.toRadians(lat2 - lat1)
    val dLon = Math.toRadians(lon2 - lon1)
    val a = sin(dLat / 2) * sin(dLat / 2) +
            cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) * sin(dLon / 2) * sin(dLon / 2)
    return RADIO_TIERRA_METROS * 2 * atan2(sqrt(a), sqrt(1 - a))
}

/** Formatea metros como "850 m" o "1.2 km". */
fun formatearDistancia(metros: Double): String =
    if (metros < 1000) "${metros.toInt()} m" else String.format(java.util.Locale.US, "%.1f km", metros / 1000)
