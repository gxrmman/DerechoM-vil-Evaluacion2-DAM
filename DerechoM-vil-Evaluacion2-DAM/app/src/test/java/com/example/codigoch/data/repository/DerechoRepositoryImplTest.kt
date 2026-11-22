package com.example.codigoch.data.repository

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class DerechoRepositoryImplTest {

    private lateinit var repository: DerechoRepositoryImpl

    @Before
    fun setUp() {
        repository = DerechoRepositoryImpl()
    }

    @Test
    fun getDerechos_devuelveLosDerechosIniciales() {
        assertEquals(6, repository.getDerechos().size)
    }

    @Test
    fun getDerechoById_conIdExistente_devuelveElDerecho() {
        assertEquals("Derecho a la Educación", repository.getDerechoById("3")?.titulo)
    }

    @Test
    fun getDerechoById_conIdInexistente_devuelveNull() {
        assertNull(repository.getDerechoById("999"))
    }

    @Test
    fun getDerechosByCategoria_filtraSoloLosDeEsaCategoria() {
        val laborales = repository.getDerechosByCategoria("laborales")
        assertTrue(laborales.isNotEmpty())
        assertTrue(laborales.all { it.categoriaId == "laborales" })
    }

    @Test
    fun toggleFavorito_cambiaElEstadoDeFavorito() {
        val antes = repository.getDerechoById("5")!!.isFavorito

        val actualizado = repository.toggleFavorito("5")

        assertNotNull(actualizado)
        assertEquals(!antes, actualizado!!.isFavorito)
        assertEquals(!antes, repository.getDerechoById("5")!!.isFavorito)
    }

    @Test
    fun toggleFavorito_dosVeces_vuelveAlEstadoOriginal() {
        val antes = repository.getDerechoById("1")!!.isFavorito

        repository.toggleFavorito("1")
        repository.toggleFavorito("1")

        assertEquals(antes, repository.getDerechoById("1")!!.isFavorito)
    }

    @Test
    fun toggleFavorito_conIdInexistente_devuelveNull() {
        assertNull(repository.toggleFavorito("999"))
    }

    @Test
    fun getFavoritos_soloIncluyeDerechosMarcados() {
        assertTrue(repository.getFavoritos().all { it.isFavorito })
    }

    @Test
    fun searchDerechos_conTextoVacio_devuelveListaVacia() {
        assertTrue(repository.searchDerechos("   ").isEmpty())
    }

    @Test
    fun searchDerechos_ignoraMayusculasYEncuentraPorTitulo() {
        val resultados = repository.searchDerechos("privacidad")
        assertTrue(resultados.any { it.titulo == "Derecho a la Privacidad" })
    }

    @Test
    fun searchDerechos_sinCoincidencias_devuelveListaVacia() {
        assertFalse(repository.searchDerechos("zzzzzz").isNotEmpty())
    }

    @Test
    fun searchCategorias_conTextoVacio_devuelveTodasLasCategorias() {
        assertEquals(repository.getCategorias().size, repository.searchCategorias("").size)
    }

    @Test
    fun searchCategorias_filtraPorNombre() {
        val resultados = repository.searchCategorias("digitales")
        assertEquals(1, resultados.size)
        assertEquals("digitales", resultados.first().id)
    }
}
