package com.example.codigoch.util

import org.junit.Assert.assertEquals
import org.junit.Test

class DistanciaUtilsTest {

    @Test
    fun calcularDistanciaMetros_mismoPunto_esCero() {
        assertEquals(0.0, calcularDistanciaMetros(-33.44, -70.65, -33.44, -70.65), 0.001)
    }

    @Test
    fun calcularDistanciaMetros_unGradoDeLatitud_esAproximadamente111Km() {
        val metros = calcularDistanciaMetros(0.0, 0.0, 1.0, 0.0)
        assertEquals(111_195.0, metros, 200.0)
    }

    @Test
    fun formatearDistancia_menosDeUnKilometro_usaMetros() {
        assertEquals("850 m", formatearDistancia(850.4))
    }

    @Test
    fun formatearDistancia_masDeUnKilometro_usaKilometros() {
        assertEquals("1.5 km", formatearDistancia(1500.0))
    }
}
