package com.example.codigoch.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.codigoch.data.remote.ConsultaDto
import com.example.codigoch.data.remote.OrientadorDto
import com.example.codigoch.data.remote.RespuestaDto
import com.example.codigoch.data.remote.RetrofitClient
import com.example.codigoch.data.repository.ConsultaRepository
import com.example.codigoch.data.repository.ConsultaRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ConsultasUiState(
    val consultas: List<ConsultaDto> = emptyList(),
    val orientadores: List<OrientadorDto> = emptyList(),
    val consultaSeleccionada: ConsultaDto? = null,
    val respuestas: List<RespuestaDto> = emptyList(),
    val cargando: Boolean = false,
    val mensaje: String? = null
)

class ConsultasViewModel(
    private val repository: ConsultaRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ConsultasUiState())
    val uiState: StateFlow<ConsultasUiState> = _uiState.asStateFlow()

    fun cargarConsultas() {
        viewModelScope.launch {
            _uiState.update { it.copy(cargando = true) }
            repository.obtenerConsultas(USUARIO_ID)
                .onSuccess { lista -> _uiState.update { it.copy(consultas = lista, cargando = false) } }
                .onFailure { _uiState.update { it.copy(cargando = false, mensaje = MENSAJE_SIN_CONEXION) } }
        }
    }

    fun cargarOrientadores() {
        viewModelScope.launch {
            repository.obtenerOrientadores()
                .onSuccess { lista -> _uiState.update { it.copy(orientadores = lista) } }
                .onFailure { _uiState.update { it.copy(mensaje = MENSAJE_SIN_CONEXION) } }
        }
    }

    /** Carga el detalle de una consulta y sus respuestas. */
    fun seleccionarConsulta(consultaId: Int) {
        viewModelScope.launch {
            repository.obtenerConsulta(consultaId)
                .onSuccess { consulta -> _uiState.update { it.copy(consultaSeleccionada = consulta) } }
                .onFailure { _uiState.update { it.copy(mensaje = MENSAJE_SIN_CONEXION) } }
            repository.obtenerRespuestas(consultaId)
                .onSuccess { lista -> _uiState.update { it.copy(respuestas = lista) } }
                .onFailure { _uiState.update { it.copy(respuestas = emptyList()) } }
        }
    }

    fun cerrarDetalle() {
        _uiState.update { it.copy(consultaSeleccionada = null, respuestas = emptyList()) }
    }

    fun crearConsulta(titulo: String, descripcion: String) {
        val error = validarConsulta(titulo, descripcion)
        if (error != null) {
            _uiState.update { it.copy(mensaje = error) }
            return
        }
        viewModelScope.launch {
            repository.crearConsulta(ConsultaDto(userId = USUARIO_ID, title = titulo.trim(), body = descripcion.trim()))
                .onSuccess { creada ->
                    _uiState.update { estado ->
                        // La API de pruebas siempre responde el mismo id, por eso se asegura que sea único en la lista
                        val idUnico = maxOf(creada.id ?: 0, (estado.consultas.maxOfOrNull { it.id ?: 0 } ?: 0) + 1)
                        estado.copy(consultas = listOf(creada.copy(id = idUnico)) + estado.consultas, mensaje = "Consulta enviada")
                    }
                }
                .onFailure { _uiState.update { it.copy(mensaje = "No se pudo enviar la consulta") } }
        }
    }

    fun editarConsulta(consultaId: Int, titulo: String, descripcion: String) {
        val error = validarConsulta(titulo, descripcion)
        if (error != null) {
            _uiState.update { it.copy(mensaje = error) }
            return
        }
        viewModelScope.launch {
            repository.editarConsulta(consultaId, ConsultaDto(consultaId, USUARIO_ID, titulo.trim(), descripcion.trim()))
                .onSuccess { editada ->
                    _uiState.update { estado ->
                        estado.copy(
                            consultas = estado.consultas.map { if (it.id == consultaId) editada else it },
                            mensaje = "Consulta actualizada"
                        )
                    }
                }
                .onFailure { _uiState.update { it.copy(mensaje = "No se pudo actualizar la consulta") } }
        }
    }

    fun eliminarConsulta(consultaId: Int) {
        viewModelScope.launch {
            repository.eliminarConsulta(consultaId)
                .onSuccess {
                    _uiState.update { estado ->
                        estado.copy(
                            consultas = estado.consultas.filterNot { it.id == consultaId },
                            mensaje = "Consulta eliminada"
                        )
                    }
                }
                .onFailure { _uiState.update { it.copy(mensaje = "No se pudo eliminar la consulta") } }
        }
    }

    fun mensajeMostrado() {
        _uiState.update { it.copy(mensaje = null) }
    }

    /** Devuelve el texto del error de validación o null si los datos son correctos. */
    private fun validarConsulta(titulo: String, descripcion: String): String? = when {
        titulo.isBlank() -> "El título es obligatorio"
        titulo.trim().length < LARGO_MINIMO_TITULO -> "El título debe tener al menos $LARGO_MINIMO_TITULO caracteres"
        descripcion.isBlank() -> "Describe tu consulta"
        else -> null
    }

    companion object {
        const val USUARIO_ID = 1
        const val LARGO_MINIMO_TITULO = 5
        const val MENSAJE_SIN_CONEXION = "No se pudo conectar con el servidor"

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer { ConsultasViewModel(ConsultaRepositoryImpl(RetrofitClient.apiService)) }
        }
    }
}
