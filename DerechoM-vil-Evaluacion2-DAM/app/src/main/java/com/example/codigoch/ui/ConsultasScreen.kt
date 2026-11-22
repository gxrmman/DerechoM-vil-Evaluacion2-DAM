package com.example.codigoch.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.codigoch.data.remote.ConsultaDto
import com.example.codigoch.data.remote.OrientadorDto
import com.example.codigoch.viewmodel.ConsultasViewModel

private val ColorPrimario = Color(0xFF1E3A8A)
private val ColorTextoTitulo = Color(0xFF0F172A)
private val ColorTextoSecundario = Color(0xFF64748B)

@Composable
fun ConsultasScreen(
    viewModel: ConsultasViewModel,
    onBackClick: () -> Unit
) {
    val estado by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var tabSeleccionada by remember { mutableIntStateOf(0) }
    var mostrarFormulario by remember { mutableStateOf(false) }
    var consultaEnEdicion by remember { mutableStateOf<ConsultaDto?>(null) }

    LaunchedEffect(Unit) {
        viewModel.cargarConsultas()
        viewModel.cargarOrientadores()
    }

    LaunchedEffect(estado.mensaje) {
        estado.mensaje?.let { mensaje ->
            snackbarHostState.showSnackbar(mensaje)
            viewModel.mensajeMostrado()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F9FC))
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 12.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver",
                        tint = Color(0xFF1E293B)
                    )
                }
                Text(
                    text = "Consultas legales",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E293B)
                )
            }

            TabRow(selectedTabIndex = tabSeleccionada, containerColor = Color.White) {
                Tab(
                    selected = tabSeleccionada == 0,
                    onClick = { tabSeleccionada = 0 },
                    text = { Text("Mis consultas") }
                )
                Tab(
                    selected = tabSeleccionada == 1,
                    onClick = { tabSeleccionada = 1 },
                    text = { Text("Orientadores") }
                )
            }

            if (tabSeleccionada == 0) {
                ListaConsultas(
                    consultas = estado.consultas,
                    cargando = estado.cargando,
                    onVerDetalle = { id -> viewModel.seleccionarConsulta(id) },
                    onEditar = {
                        consultaEnEdicion = it
                        mostrarFormulario = true
                    },
                    onEliminar = { id -> viewModel.eliminarConsulta(id) }
                )
            } else {
                ListaOrientadores(orientadores = estado.orientadores)
            }
        }

        if (tabSeleccionada == 0) {
            FloatingActionButton(
                onClick = {
                    consultaEnEdicion = null
                    mostrarFormulario = true
                },
                containerColor = ColorPrimario,
                contentColor = Color.White,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(20.dp)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Nueva consulta")
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        )
    }

    if (mostrarFormulario) {
        FormularioConsulta(
            consulta = consultaEnEdicion,
            onCancelar = { mostrarFormulario = false },
            onGuardar = { titulo, descripcion ->
                val consulta = consultaEnEdicion
                if (consulta?.id != null) {
                    viewModel.editarConsulta(consulta.id, titulo, descripcion)
                } else {
                    viewModel.crearConsulta(titulo, descripcion)
                }
                mostrarFormulario = false
            }
        )
    }

    estado.consultaSeleccionada?.let { consulta ->
        AlertDialog(
            onDismissRequest = { viewModel.cerrarDetalle() },
            confirmButton = {
                TextButton(onClick = { viewModel.cerrarDetalle() }) { Text("Cerrar") }
            },
            title = { Text(text = consulta.title, fontSize = 16.sp, fontWeight = FontWeight.Bold) },
            text = {
                Column(
                    modifier = Modifier
                        .heightIn(max = 360.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(text = consulta.body, fontSize = 14.sp, color = ColorTextoSecundario)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Respuestas (${estado.respuestas.size})",
                        fontWeight = FontWeight.Bold,
                        color = ColorTextoTitulo
                    )
                    estado.respuestas.forEach { respuesta ->
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = respuesta.email, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = ColorPrimario)
                        Text(text = respuesta.body, fontSize = 13.sp, color = ColorTextoSecundario)
                    }
                }
            }
        )
    }
}

@Composable
private fun ListaConsultas(
    consultas: List<ConsultaDto>,
    cargando: Boolean,
    onVerDetalle: (Int) -> Unit,
    onEditar: (ConsultaDto) -> Unit,
    onEliminar: (Int) -> Unit
) {
    when {
        cargando && consultas.isEmpty() -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = ColorPrimario)
            }
        }

        consultas.isEmpty() -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Aún no tienes consultas", color = ColorTextoSecundario)
            }
        }

        else -> {
            LazyColumn(
                contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(consultas) { consulta ->
                    TarjetaConsulta(
                        consulta = consulta,
                        onVerDetalle = { consulta.id?.let(onVerDetalle) },
                        onEditar = { onEditar(consulta) },
                        onEliminar = { consulta.id?.let(onEliminar) }
                    )
                }
            }
        }
    }
}

@Composable
private fun TarjetaConsulta(
    consulta: ConsultaDto,
    onVerDetalle: () -> Unit,
    onEditar: () -> Unit,
    onEliminar: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onVerDetalle() }
    ) {
        Row(
            modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = consulta.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = ColorTextoTitulo,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = consulta.body,
                    fontSize = 13.sp,
                    color = ColorTextoSecundario,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            IconButton(onClick = onEditar) {
                Icon(Icons.Default.Edit, contentDescription = "Editar", tint = ColorPrimario)
            }
            IconButton(onClick = onEliminar) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color(0xFFDC2626))
            }
        }
    }
}

@Composable
private fun ListaOrientadores(orientadores: List<OrientadorDto>) {
    if (orientadores.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = ColorPrimario)
        }
        return
    }

    LazyColumn(
        contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(orientadores) { orientador ->
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = orientador.name,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = ColorTextoTitulo
                    )
                    orientador.company?.name?.let {
                        Text(text = it, fontSize = 12.sp, color = ColorPrimario)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = orientador.email, fontSize = 13.sp, color = ColorTextoSecundario)
                    orientador.phone?.let {
                        Text(text = it, fontSize = 13.sp, color = ColorTextoSecundario)
                    }
                }
            }
        }
    }
}

@Composable
private fun FormularioConsulta(
    consulta: ConsultaDto?,
    onCancelar: () -> Unit,
    onGuardar: (titulo: String, descripcion: String) -> Unit
) {
    var titulo by remember { mutableStateOf(consulta?.title ?: "") }
    var descripcion by remember { mutableStateOf(consulta?.body ?: "") }

    val tituloInvalido = titulo.isNotEmpty() && titulo.trim().length < ConsultasViewModel.LARGO_MINIMO_TITULO
    val formularioValido = titulo.trim().length >= ConsultasViewModel.LARGO_MINIMO_TITULO && descripcion.isNotBlank()

    AlertDialog(
        onDismissRequest = onCancelar,
        title = { Text(if (consulta == null) "Nueva consulta" else "Editar consulta") },
        text = {
            Column {
                OutlinedTextField(
                    value = titulo,
                    onValueChange = { titulo = it },
                    label = { Text("Título") },
                    singleLine = true,
                    isError = tituloInvalido,
                    supportingText = {
                        if (tituloInvalido) Text("Mínimo ${ConsultasViewModel.LARGO_MINIMO_TITULO} caracteres")
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Describe tu consulta") },
                    minLines = 3,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onGuardar(titulo, descripcion) },
                enabled = formularioValido
            ) { Text("Guardar") }
        },
        dismissButton = {
            TextButton(onClick = onCancelar) { Text("Cancelar") }
        }
    )
}
