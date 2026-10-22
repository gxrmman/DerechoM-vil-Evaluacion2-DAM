package com.example.codigoch.model

data class CategoriaDerecho(
    val id: String,
    val nombre: String,
    val descripcion: String,
    val cantidadDerechos: Int,
    val emoji: String,
    val colorHex: Long
)

data class Derecho(
    val id: String,
    val titulo: String,
    val categoriaId: String,
    val categoriaNombre: String,
    val descripcion: String,
    val queDiceLaLey: String,
    val ejemplos: List<String>,
    val emoji: String,
    val isFavorito: Boolean = false
)
