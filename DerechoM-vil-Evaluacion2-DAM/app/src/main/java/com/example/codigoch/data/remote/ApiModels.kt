package com.example.codigoch.data.remote

/** Consulta legal enviada por el usuario (recurso "posts" de la API). */
data class ConsultaDto(
    val id: Int? = null,
    val userId: Int,
    val title: String,
    val body: String
)

/** Respuesta recibida para una consulta (recurso "comments" de la API). */
data class RespuestaDto(
    val id: Int,
    val postId: Int,
    val name: String,
    val email: String,
    val body: String
)

/** Orientador legal disponible (recurso "users" de la API). */
data class OrientadorDto(
    val id: Int,
    val name: String,
    val email: String,
    val phone: String? = null,
    val company: EmpresaDto? = null
)

data class EmpresaDto(
    val name: String? = null
)
