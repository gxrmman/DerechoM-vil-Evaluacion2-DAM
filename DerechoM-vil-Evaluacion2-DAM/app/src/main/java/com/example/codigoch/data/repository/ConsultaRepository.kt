package com.example.codigoch.data.repository

import com.example.codigoch.data.remote.ApiService
import com.example.codigoch.data.remote.ConsultaDto
import com.example.codigoch.data.remote.OrientadorDto
import com.example.codigoch.data.remote.RespuestaDto

/** Acceso a las consultas legales, respuestas y orientadores que entrega la API de Xano. */
interface ConsultaRepository {
    suspend fun obtenerConsultas(usuarioId: Int): Result<List<ConsultaDto>>
    suspend fun obtenerConsulta(consultaId: Int): Result<ConsultaDto>
    suspend fun obtenerRespuestas(consultaId: Int): Result<List<RespuestaDto>>
    suspend fun obtenerOrientadores(): Result<List<OrientadorDto>>
    suspend fun crearConsulta(consulta: ConsultaDto): Result<ConsultaDto>
    suspend fun editarConsulta(consultaId: Int, consulta: ConsultaDto): Result<ConsultaDto>
    suspend fun eliminarConsulta(consultaId: Int): Result<Unit>
}

class ConsultaRepositoryImpl(
    private val apiService: ApiService
) : ConsultaRepository {

    override suspend fun obtenerConsultas(usuarioId: Int) =
        runCatching { apiService.obtenerConsultas().filter { it.userId == usuarioId }.sortedByDescending { it.id } }

    override suspend fun obtenerConsulta(consultaId: Int) =
        runCatching { apiService.obtenerConsulta(consultaId) }

    override suspend fun obtenerRespuestas(consultaId: Int) =
        runCatching { apiService.obtenerRespuestas().filter { it.consultaId == consultaId } }

    override suspend fun obtenerOrientadores() =
        runCatching { apiService.obtenerOrientadores() }

    override suspend fun crearConsulta(consulta: ConsultaDto) =
        runCatching { apiService.crearConsulta(consulta) }

    override suspend fun editarConsulta(consultaId: Int, consulta: ConsultaDto) =
        runCatching { apiService.editarConsulta(consultaId, consulta) }

    override suspend fun eliminarConsulta(consultaId: Int) = runCatching {
        val respuesta = apiService.eliminarConsulta(consultaId)
        if (!respuesta.isSuccessful) throw IllegalStateException("Error ${respuesta.code()} al eliminar")
    }
}
