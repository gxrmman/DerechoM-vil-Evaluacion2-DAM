package com.example.codigoch.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

/** Endpoints de la API en Xano (consultas legales, respuestas y orientadores). */
interface ApiService {

    // 1. GET: lista de consultas
    @GET("consulta")
    suspend fun obtenerConsultas(): List<ConsultaDto>

    // 2. GET: detalle de una consulta
    @GET("consulta/{id}")
    suspend fun obtenerConsulta(@Path("id") consultaId: Int): ConsultaDto

    // 3. GET: respuestas de las consultas
    @GET("respuesta")
    suspend fun obtenerRespuestas(): List<RespuestaDto>

    // 4. GET: orientadores legales
    @GET("orientador")
    suspend fun obtenerOrientadores(): List<OrientadorDto>

    // 5. POST: crear una consulta
    @POST("consulta")
    suspend fun crearConsulta(@Body consulta: ConsultaDto): ConsultaDto

    // 6. PATCH: editar una consulta
    @PATCH("consulta/{id}")
    suspend fun editarConsulta(@Path("id") consultaId: Int, @Body consulta: ConsultaDto): ConsultaDto

    // 7. DELETE: eliminar una consulta
    @DELETE("consulta/{id}")
    suspend fun eliminarConsulta(@Path("id") consultaId: Int): Response<Unit>
}
