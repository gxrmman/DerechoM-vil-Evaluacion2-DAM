package com.example.codigoch.data.repository

import android.graphics.Bitmap
import com.example.codigoch.data.SampleData
import com.example.codigoch.model.CategoriaDerecho
import com.example.codigoch.model.Derecho

interface DerechoRepository {
    fun getCategorias(): List<CategoriaDerecho>
    fun getDerechos(): List<Derecho>
    fun getFavoritos(): List<Derecho>
    fun getDerechoById(id: String): Derecho?
    fun getDerechosByCategoria(categoriaId: String): List<Derecho>
    fun toggleFavorito(derechoId: String): Derecho?
    fun searchDerechos(query: String): List<Derecho>
    fun searchCategorias(query: String): List<CategoriaDerecho>
    fun getNotePhoto(derechoId: String): Bitmap?
    fun saveNotePhoto(derechoId: String, bitmap: Bitmap)
}

class DerechoRepositoryImpl : DerechoRepository {

    private val derechos = SampleData.derechosIniciales.toMutableList()
    private val categorias = SampleData.categorias.toList()
    private val notePhotos = mutableMapOf<String, Bitmap>()

    override fun getCategorias(): List<CategoriaDerecho> {
        return categorias
    }

    override fun getDerechos(): List<Derecho> {
        return derechos.toList()
    }

    override fun getFavoritos(): List<Derecho> {
        return derechos.filter { it.isFavorito }
    }

    override fun getDerechoById(id: String): Derecho? {
        return derechos.find { it.id == id }
    }

    override fun getDerechosByCategoria(categoriaId: String): List<Derecho> {
        return derechos.filter { it.categoriaId == categoriaId }
    }

    override fun toggleFavorito(derechoId: String): Derecho? {
        val index = derechos.indexOfFirst { it.id == derechoId }
        if (index != -1) {
            val item = derechos[index]
            val updated = item.copy(isFavorito = !item.isFavorito)
            derechos[index] = updated
            return updated
        }
        return null
    }

    override fun searchDerechos(query: String): List<Derecho> {
        if (query.isBlank()) return emptyList()
        return derechos.filter {
            it.titulo.contains(query, ignoreCase = true) ||
                    it.descripcion.contains(query, ignoreCase = true) ||
                    it.queDiceLaLey.contains(query, ignoreCase = true)
        }
    }

    override fun searchCategorias(query: String): List<CategoriaDerecho> {
        if (query.isBlank()) return categorias
        return categorias.filter {
            it.nombre.contains(query, ignoreCase = true) ||
                    it.descripcion.contains(query, ignoreCase = true)
        }
    }

    override fun getNotePhoto(derechoId: String): Bitmap? {
        return notePhotos[derechoId]
    }

    override fun saveNotePhoto(derechoId: String, bitmap: Bitmap) {
        notePhotos[derechoId] = bitmap
    }
}
