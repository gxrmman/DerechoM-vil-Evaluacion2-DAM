package com.example.codigoch.data.remote

import com.google.gson.annotations.SerializedName

/** Consulta legal enviada por el usuario (tabla "consulta" en Xano). */
data class ConsultaDto(
    val id: Int? = null,
    @SerializedName("user_id") val userId: Int,
    val title: String,
    val body: String
)

/** Respuesta recibida para una consulta (tabla "respuesta" en Xano). */
data class RespuestaDto(
    val id: Int,
    @SerializedName("consulta_id") val consultaId: Int,
    val name: String,
    val email: String,
    val body: String
)

/** Orientador legal disponible (tabla "orientador" en Xano). */
data class OrientadorDto(
    val id: Int,
    val name: String,
    val email: String,
    val phone: String? = null,
    val institucion: String? = null
)
