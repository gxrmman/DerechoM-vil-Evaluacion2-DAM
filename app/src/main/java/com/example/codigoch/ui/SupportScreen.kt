package com.example.codigoch.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class FaqItem(
    val pregunta: String,
    val respuesta: String
)

val faqsList = listOf(
    FaqItem(
        pregunta = "¿Cómo puedo ejercer un derecho?",
        respuesta = "Para ejercer un derecho, primero debes informarte sobre él. Luego, puedes acudir a las instituciones correspondientes o buscar orientación legal."
    ),
    FaqItem(
        pregunta = "¿Qué hago si se vulnera mi derecho?",
        respuesta = "Puedes realizar una denuncia o reclamo en la institución pertinente (como la Inspección del Trabajo, SERNAC, o Tribunales de Justicia) o solicitar orientación legal."
    ),
    FaqItem(
        pregunta = "¿Dónde puedo acudir para recibir ayuda?",
        respuesta = "Existen Corporaciones de Asistencia Judicial, la Defensoría de la Niñez, el Instituto Nacional de Derechos Humanos (INDH) y diversas ONG de asistencia legal."
    ),
    FaqItem(
        pregunta = "¿Qué derechos tengo como estudiante?",
        respuesta = "Tienes derecho a una educación inclusiva, a ser escuchado, a la libertad de expresión y a un ambiente seguro libre de violencia o acoso escolar."
    ),
    FaqItem(
        pregunta = "¿Cuáles son mis derechos laborales?",
        respuesta = "Derecho a un contrato escrito, remuneración mínima, jornada máxima de trabajo, descansos, vacaciones pagadas y seguridad social."
    ),
    FaqItem(
        pregunta = "¿Qué derechos tienen los niños y adolescentes?",
        respuesta = "Derecho a la vida, desarrollo integral, salud, educación, vivir en familia y ser protegidos contra toda forma de abuso o explotación."
    ),
    FaqItem(
        pregunta = "¿Cómo protejo mis datos personales?",
        respuesta = "No compartas contraseñas, verifica la privacidad en redes sociales y exige la eliminación de tu información cuando ya no sea requerida."
    )
)

@Composable
fun SupportScreen(
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F9FC))
    ) {
        // Top bar
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
                text = "Preguntas frecuentes",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E293B)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "🎧",
                fontSize = 48.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "¿Necesitas ayuda?",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Estamos aquí para ayudarte. Si tienes dudas sobre tus derechos o necesitas orientación, puedes contactarnos.",
                fontSize = 14.sp,
                color = Color(0xFF64748B),
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Botones de contacto
            Button(
                onClick = { },
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E3A8A)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Message,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Chat en línea", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
                onClick = { },
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF1E3A8A)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Call,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Llamar", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
                onClick = { },
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF1E3A8A)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Correo electrónico", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "Preguntas frecuentes",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A),
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Lista expandible de FAQs
            faqsList.forEach { faq ->
                FaqExpandableCard(faq = faq)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun FaqExpandableCard(faq: FaqItem) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = faq.pregunta,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1E293B),
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = Color(0xFF64748B)
                )
            }

            AnimatedVisibility(visible = expanded) {
                Column {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = faq.respuesta,
                        fontSize = 13.sp,
                        color = Color(0xFF64748B),
                        lineHeight = 18.sp
                    )
                }
            }
        }
    }
}
