package com.example.codigoch.viewmodel

import com.example.codigoch.data.remote.ConsultaDto
import com.example.codigoch.data.remote.OrientadorDto
import com.example.codigoch.data.remote.RespuestaDto
import com.example.codigoch.data.repository.ConsultaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ConsultasViewModelTest {

    /** Repositorio falso que permite simular éxito o error de red. */
    private class FakeConsultaRepository(var hayError: Boolean = false) : ConsultaRepository {
        var llamadasACrear = 0

        private val consultasIniciales = listOf(
            ConsultaDto(1, 1, "Primera consulta", "Texto uno"),
            ConsultaDto(2, 1, "Segunda consulta", "Texto dos")
        )

        private fun <T> resultado(valor: T): Result<T> =
            if (hayError) Result.failure(RuntimeException("Error de red")) else Result.success(valor)

        override suspend fun obtenerConsultas(usuarioId: Int) = resultado(consultasIniciales)
        override suspend fun obtenerConsulta(consultaId: Int) = resultado(consultasIniciales.first { it.id == consultaId })
        override suspend fun obtenerRespuestas(consultaId: Int) =
            resultado(listOf(RespuestaDto(1, consultaId, "Re", "a@b.cl", "Respuesta")))
        override suspend fun obtenerOrientadores() = resultado(listOf(OrientadorDto(1, "Ana", "ana@correo.cl")))
        override suspend fun crearConsulta(consulta: ConsultaDto): Result<ConsultaDto> {
            llamadasACrear++
            return resultado(consulta.copy(id = 101))
        }
        override suspend fun editarConsulta(consultaId: Int, consulta: ConsultaDto) = resultado(consulta.copy(id = consultaId))
        override suspend fun eliminarConsulta(consultaId: Int) = resultado(Unit)
    }

    private lateinit var repository: FakeConsultaRepository
    private lateinit var viewModel: ConsultasViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        repository = FakeConsultaRepository()
        viewModel = ConsultasViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun cargarConsultas_conExito_llenaLaLista() {
        viewModel.cargarConsultas()

        assertEquals(2, viewModel.uiState.value.consultas.size)
        assertFalse(viewModel.uiState.value.cargando)
    }

    @Test
    fun cargarConsultas_conError_muestraMensaje() {
        repository.hayError = true

        viewModel.cargarConsultas()

        assertTrue(viewModel.uiState.value.consultas.isEmpty())
        assertEquals(ConsultasViewModel.MENSAJE_SIN_CONEXION, viewModel.uiState.value.mensaje)
    }

    @Test
    fun cargarOrientadores_conExito_llenaLaLista() {
        viewModel.cargarOrientadores()

        assertEquals("Ana", viewModel.uiState.value.orientadores.first().name)
    }

    @Test
    fun seleccionarConsulta_cargaDetalleYRespuestas() {
        viewModel.seleccionarConsulta(2)

        assertEquals("Segunda consulta", viewModel.uiState.value.consultaSeleccionada?.title)
        assertEquals(1, viewModel.uiState.value.respuestas.size)
    }

    @Test
    fun cerrarDetalle_limpiaLaSeleccion() {
        viewModel.seleccionarConsulta(1)

        viewModel.cerrarDetalle()

        assertNull(viewModel.uiState.value.consultaSeleccionada)
        assertTrue(viewModel.uiState.value.respuestas.isEmpty())
    }

    @Test
    fun crearConsulta_conTituloVacio_noLlamaALaApi() {
        viewModel.crearConsulta("   ", "Descripción")

        assertEquals(0, repository.llamadasACrear)
        assertEquals("El título es obligatorio", viewModel.uiState.value.mensaje)
    }

    @Test
    fun crearConsulta_conTituloCorto_muestraError() {
        viewModel.crearConsulta("abc", "Descripción")

        assertEquals(0, repository.llamadasACrear)
        assertTrue(viewModel.uiState.value.mensaje!!.contains("al menos"))
    }

    @Test
    fun crearConsulta_conDescripcionVacia_muestraError() {
        viewModel.crearConsulta("Título válido", "")

        assertEquals("Describe tu consulta", viewModel.uiState.value.mensaje)
    }

    @Test
    fun crearConsulta_valida_seAgregaAlInicioDeLaLista() {
        viewModel.cargarConsultas()

        viewModel.crearConsulta("Título válido", "Descripción")

        val consultas = viewModel.uiState.value.consultas
        assertEquals(3, consultas.size)
        assertEquals("Título válido", consultas.first().title)
    }

    @Test
    fun crearConsulta_dosVeces_generaIdsUnicos() {
        viewModel.crearConsulta("Primera nueva", "Texto")
        viewModel.crearConsulta("Segunda nueva", "Texto")

        val ids = viewModel.uiState.value.consultas.map { it.id }
        assertEquals(ids.size, ids.toSet().size)
    }

    @Test
    fun editarConsulta_actualizaSoloLaConsultaIndicada() {
        viewModel.cargarConsultas()

        viewModel.editarConsulta(1, "Título editado", "Texto editado")

        val consultas = viewModel.uiState.value.consultas
        assertEquals("Título editado", consultas.first { it.id == 1 }.title)
        assertEquals("Segunda consulta", consultas.first { it.id == 2 }.title)
    }

    @Test
    fun eliminarConsulta_laQuitaDeLaLista() {
        viewModel.cargarConsultas()

        viewModel.eliminarConsulta(1)

        assertEquals(listOf(2), viewModel.uiState.value.consultas.map { it.id })
    }

    @Test
    fun eliminarConsulta_conError_noModificaLaLista() {
        viewModel.cargarConsultas()
        repository.hayError = true

        viewModel.eliminarConsulta(1)

        assertEquals(2, viewModel.uiState.value.consultas.size)
        assertEquals("No se pudo eliminar la consulta", viewModel.uiState.value.mensaje)
    }

    @Test
    fun mensajeMostrado_limpiaElMensaje() {
        viewModel.crearConsulta("", "")

        viewModel.mensajeMostrado()

        assertNull(viewModel.uiState.value.mensaje)
    }
}
