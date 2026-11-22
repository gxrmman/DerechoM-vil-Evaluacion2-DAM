package com.example.codigoch.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

/** Endpoints de la API externa (https://jsonplaceholder.typicode.com). */
interface ApiService {

    // 1. GET: lista de consultas del usuario
    @GET("posts")
    suspend fun obtenerConsultas(@Query("userId") usuarioId: Int): List<ConsultaDto>

    // 2. GET: detalle de una consulta
    @GET("posts/{id}")
    suspend fun obtenerConsulta(@Path("id") consultaId: Int): ConsultaDto

    // 3. GET: respuestas de una consulta
    @GET("posts/{id}/comments")
    suspend fun obtenerRespuestas(@Path("id") consultaId: Int): List<RespuestaDto>

    // 4. GET: orientadores legales
    @GET("users")
    suspend fun obtenerOrientadores(): List<OrientadorDto>

    // 5. POST: crear una consulta
    @POST("posts")
    suspend fun crearConsulta(@Body consulta: ConsultaDto): ConsultaDto

    // 6. PUT: editar una consulta
    @PUT("posts/{id}")
    suspend fun editarConsulta(@Path("id") consultaId: Int, @Body consulta: ConsultaDto): ConsultaDto

    // 7. DELETE: eliminar una consulta
    @DELETE("posts/{id}")
    suspend fun eliminarConsulta(@Path("id") consultaId: Int): Response<Unit>
}
