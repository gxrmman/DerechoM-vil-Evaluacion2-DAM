package com.example.codigoch.data

import com.example.codigoch.model.CategoriaDerecho
import com.example.codigoch.model.Derecho

object SampleData {
    val categorias = listOf(
        CategoriaDerecho(
            id = "humanos",
            nombre = "Derechos Humanos",
            descripcion = "Dignidad, igualdad y libertad para todas las personas.",
            cantidadDerechos = 12,
            emoji = "👥",
            colorHex = 0xFF7C3AED
        ),
        CategoriaDerecho(
            id = "laborales",
            nombre = "Derechos Laborales",
            descripcion = "Trabajo justo, seguro y sin discriminación.",
            cantidadDerechos = 10,
            emoji = "👷",
            colorHex = 0xFF10B981
        ),
        CategoriaDerecho(
            id = "ninez",
            nombre = "Derechos de la Niñez y Adolescencia",
            descripcion = "Protección y desarrollo integral.",
            cantidadDerechos = 8,
            emoji = "🧑‍🤝‍🧑",
            colorHex = 0xFFF59E0B
        ),
        CategoriaDerecho(
            id = "estudiantiles",
            nombre = "Derechos Estudiantiles",
            descripcion = "Educación, participación y bienestar.",
            cantidadDerechos = 7,
            emoji = "🎓",
            colorHex = 0xFF3B82F6
        ),
        CategoriaDerecho(
            id = "digitales",
            nombre = "Derechos Digitales",
            descripcion = "Privacidad, seguridad y acceso a internet.",
            cantidadDerechos = 9,
            emoji = "🔒",
            colorHex = 0xFF14B8A6
        ),
        CategoriaDerecho(
            id = "otros",
            nombre = "Otros Derechos",
            descripcion = "Vivienda, salud, medio ambiente, entre otros.",
            cantidadDerechos = 9,
            emoji = "🌐",
            colorHex = 0xFF6B7280
        )
    )

    val derechosIniciales = listOf(
        Derecho(
            id = "1",
            titulo = "Derecho a la Igualdad",
            categoriaId = "humanos",
            categoriaNombre = "Derechos Humanos",
            descripcion = "Todas las personas somos iguales ante la ley y tenemos los mismos derechos, sin importar nuestro origen, género, religión, orientación sexual, entre otros.",
            queDiceLaLey = "La Constitución de Chile, en su artículo 19, garantiza la igualdad ante la ley.",
            ejemplos = listOf(
                "No discriminación en el trabajo",
                "Igualdad de oportunidades en la educación",
                "Respeto a la diversidad"
            ),
            emoji = "👥",
            isFavorito = true
        ),
        Derecho(
            id = "2",
            titulo = "Derecho a un Trabajo Digno",
            categoriaId = "laborales",
            categoriaNombre = "Derechos Laborales",
            descripcion = "Toda persona tiene derecho al trabajo en condiciones equitativas, satisfactorias y a una protección efectiva contra el desempleo.",
            queDiceLaLey = "El Código del Trabajo resguarda los derechos fundamentales de los trabajadores en su espacio de empleo.",
            ejemplos = listOf(
                "Remuneración justa y a tiempo",
                "Jornada laboral regulada y descanso semanal",
                "Ambiente de trabajo libre de acoso"
            ),
            emoji = "👷",
            isFavorito = true
        ),
        Derecho(
            id = "3",
            titulo = "Derecho a la Educación",
            categoriaId = "estudiantiles",
            categoriaNombre = "Derechos Estudiantiles",
            descripcion = "Acceso a una educación inclusiva, libre y de calidad que potencie el desarrollo integral de las personas.",
            queDiceLaLey = "La Ley General de Educación establece la obligación del Estado de asegurar una educación gratuita y de calidad.",
            ejemplos = listOf(
                "Acceso sin discriminación a establecimientos",
                "Libertad de opinión y libertad de cátedra",
                "Uso de beneficios estudiantiles"
            ),
            emoji = "🎓",
            isFavorito = true
        ),
        Derecho(
            id = "4",
            titulo = "Derecho a la Privacidad",
            categoriaId = "digitales",
            categoriaNombre = "Derechos Digitales",
            descripcion = "Protección de los datos personales, inviolabilidad de las comunicaciones y seguridad en los entornos virtuales.",
            queDiceLaLey = "La Ley de Protección de Datos Personales resguarda la privacidad digital y el tratamiento razonable de tu información.",
            ejemplos = listOf(
                "Protección de tus contraseñas y mensajes",
                "Solicitud de eliminación de datos personales",
                "Navegación segura y libre de rastreo indebido"
            ),
            emoji = "🔒",
            isFavorito = true
        ),
        Derecho(
            id = "5",
            titulo = "Derecho a la Salud",
            categoriaId = "otros",
            categoriaNombre = "Otros Derechos",
            descripcion = "Garantía de acceso libre y oportuno a la atención en salud, preventiva y curativa, con estándares dignos.",
            queDiceLaLey = "El derecho a la protección de la salud está reconocido en la Constitución y las normas de salud pública.",
            ejemplos = listOf(
                "Acceso a prestaciones médicas oportunas",
                "Elección entre sistema público o privado",
                "Información clara sobre diagnósticos y tratamientos"
            ),
            emoji = "🏥",
            isFavorito = false
        ),
        Derecho(
            id = "6",
            titulo = "Protección de la Infancia",
            categoriaId = "ninez",
            categoriaNombre = "Derechos de la Niñez y Adolescencia",
            descripcion = "El interés superior del niño y niña, garantizando su derecho a la vida, salud, educación y un ambiente libre de violencia.",
            queDiceLaLey = "La Ley sobre Garantías de la Niñez reconoce a niños, niñas y adolescentes como sujetos de derecho.",
            ejemplos = listOf(
                "Ambiente familiar seguro",
                "Acceso a la educación y juego",
                "Protección contra el maltrato y explotación"
            ),
            emoji = "🧑‍🤝‍🧑",
            isFavorito = false
        )
    )
}
