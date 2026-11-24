package com.example.codigoch.data.repository

import com.example.codigoch.data.remote.ApiService
import com.example.codigoch.data.remote.ConsultaDto
import com.example.codigoch.data.remote.OrientadorDto
import com.example.codigoch.data.remote.RespuestaDto
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Test
import retrofit2.Response

class ConsultaRepositoryImplTest {

    /** API falsa: permite probar el repositorio sin usar internet. */
    private class FakeApiService(private val fallar: Boolean = false, private val codigoEliminar: Int = 200) : ApiService {
        private val consulta = ConsultaDto(id = 1, userId = 1, title = "Consulta laboral", body = "Detalle")

        private fun <T> responder(valor: T): T {
            if (fallar) throw RuntimeException("Sin conexión")
            return valor
        }

        override suspend fun obtenerConsultas() = responder(listOf(consulta, consulta.copy(id = 2, userId = 2)))
        override suspend fun obtenerConsulta(consultaId: Int) = responder(consulta)
        override suspend fun obtenerRespuestas() = responder(
            listOf(
                RespuestaDto(1, 1, "Respuesta", "a@b.cl", "Texto"),
                RespuestaDto(2, 7, "Otra", "c@d.cl", "Otro texto")
            )
        )
        override suspend fun obtenerOrientadores() = responder(listOf(OrientadorDto(1, "Ana", "ana@correo.cl")))
        override suspend fun crearConsulta(consulta: ConsultaDto) = responder(consulta.copy(id = 101))
        override suspend fun editarConsulta(consultaId: Int, consulta: ConsultaDto) = responder(consulta.copy(id = consultaId))
        override suspend fun eliminarConsulta(consultaId: Int): Response<Unit> {
            if (fallar) throw RuntimeException("Sin conexión")
            return if (codigoEliminar == 200) Response.success(Unit)
            else Response.error(codigoEliminar, "".toResponseBody(null))
        }
    }

    @Test
    fun obtenerConsultas_conRespuestaCorrecta_devuelveSuccess() = runBlocking {
        val resultado = ConsultaRepositoryImpl(FakeApiService()).obtenerConsultas(1)

        assertTrue(resultado.isSuccess)
        assertEquals(1, resultado.getOrThrow().size)
    }

    @Test
    fun obtenerConsultas_filtraSoloLasDelUsuario() = runBlocking {
        val resultado = ConsultaRepositoryImpl(FakeApiService()).obtenerConsultas(2)

        assertEquals(listOf(2), resultado.getOrThrow().map { it.id })
    }

    @Test
    fun obtenerRespuestas_filtraPorConsulta() = runBlocking {
        val resultado = ConsultaRepositoryImpl(FakeApiService()).obtenerRespuestas(1)

        assertEquals(listOf(1), resultado.getOrThrow().map { it.id })
    }

    @Test
    fun obtenerConsultas_sinConexion_devuelveFailure() = runBlocking {
        val resultado = ConsultaRepositoryImpl(FakeApiService(fallar = true)).obtenerConsultas(1)

        assertTrue(resultado.isFailure)
    }

    @Test
    fun crearConsulta_devuelveLaConsultaConIdAsignado() = runBlocking {
        val nueva = ConsultaDto(userId = 1, title = "Título nuevo", body = "Descripción")

        val resultado = ConsultaRepositoryImpl(FakeApiService()).crearConsulta(nueva)

        assertEquals(101, resultado.getOrThrow().id)
    }

    @Test
    fun editarConsulta_devuelveLaConsultaActualizada() = runBlocking {
        val editada = ConsultaDto(5, 1, "Título editado", "Nuevo texto")

        val resultado = ConsultaRepositoryImpl(FakeApiService()).editarConsulta(5, editada)

        assertEquals("Título editado", resultado.getOrThrow().title)
    }

    @Test
    fun eliminarConsulta_conCodigo200_esExitoso() = runBlocking {
        assertTrue(ConsultaRepositoryImpl(FakeApiService()).eliminarConsulta(1).isSuccess)
    }

    @Test
    fun eliminarConsulta_conCodigoDeError_devuelveFailure() = runBlocking {
        assertTrue(ConsultaRepositoryImpl(FakeApiService(codigoEliminar = 500)).eliminarConsulta(1).isFailure)
    }
}
