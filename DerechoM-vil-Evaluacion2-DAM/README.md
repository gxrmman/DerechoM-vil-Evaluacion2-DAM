# CódigoCH – Derechos al alcance de tu mano

Aplicación móvil Android (Kotlin + Jetpack Compose) que informa a las personas sobre sus derechos en Chile,
permite enviar consultas legales y ubicar puntos de atención cercanos.

Asignatura: DSY1105 – Desarrollo de Aplicaciones Móviles · Evaluación Parcial 4

## 1. Integrantes

| Integrante | GitHub |
|------------|--------|
| (completar nombre y apellido) | [@gxrmman](https://github.com/gxrmman) |
| (completar nombre y apellido) | [@michaelciudad](https://github.com/michaelciudad) |

Tablero de planificación en Trello: (pegar aquí el enlace del tablero)

## 2. Funcionalidades

- Pantalla de bienvenida, registro e inicio de sesión (SQLite local, sesión persistente).
- Catálogo de derechos por categoría, búsqueda, detalle y favoritos.
- **Consultas legales** (consume API externa): listar, ver detalle con respuestas, crear, editar y eliminar consultas, y ver orientadores disponibles.
- **Recurso nativo 1 – Cámara:** foto de perfil y foto de apuntes en el detalle de un derecho.
- **Recurso nativo 2 – GPS:** en el mapa, el botón de ubicación obtiene la posición del usuario y selecciona el punto de atención legal más cercano indicando la distancia.
- Mapa de Google Maps con puntos de atención (CAJ, INDH, Defensoría de la Niñez, etc.), llamada y ruta con Google Maps.
- Preguntas frecuentes.

## 3. Arquitectura

```
com.example.codigoch
├── data
│   ├── remote        -> ApiService (Retrofit), RetrofitClient, modelos de la API
│   └── repository    -> ConsultaRepository, DerechoRepository, UserRepository
├── viewmodel         -> ConsultasViewModel (estado con StateFlow)
├── ui                -> pantallas Compose
├── model             -> modelos locales
└── util              -> cálculo de distancia (GPS)
```

Flujo de las consultas: `ConsultasScreen` → `ConsultasViewModel` → `ConsultaRepository` → `ApiService` (Retrofit).

## 4. Endpoints utilizados

API propia creada en [Xano](https://www.xano.com) con base de datos (tablas `consulta`, `respuesta` y `orientador`), consumida desde la app con Retrofit + Gson.
URL base: `https://(instancia).xano.io/api:(grupo)/`

| # | Método | Endpoint | Uso en la app |
|---|--------|----------|---------------|
| 1 | GET | `/consulta` | Lista las consultas del usuario |
| 2 | GET | `/consulta/{id}` | Detalle de una consulta |
| 3 | GET | `/respuesta` | Respuestas de las consultas |
| 4 | GET | `/orientador` | Lista de orientadores legales |
| 5 | POST | `/consulta` | Crear una consulta |
| 6 | PATCH | `/consulta/{id}` | Editar una consulta |
| 7 | DELETE | `/consulta/{id}` | Eliminar una consulta |

Si la API no está disponible, la app muestra un mensaje de error de conexión.

## 5. Pasos para ejecutar

1. Clonar el repositorio y abrirlo con Android Studio (JDK 11 o superior).
2. Crear el archivo `local.properties` en la raíz (si no existe) y agregar la API key de Google Maps:
   ```
   MAPS_API_KEY=tu_api_key
   ```
3. Sincronizar Gradle y ejecutar en un emulador o dispositivo con Android 7.0 (API 24) o superior y conexión a internet.
4. Ejecutar las pruebas unitarias: `./gradlew testDebugUnitTest`

### Generar el APK firmado (release)

```
./gradlew assembleRelease
```

El APK queda en `app/build/outputs/apk/release/app-release.apk` (copia incluida en `release/`).

## 6. APK firmado y llave (.jks)

- Keystore: `app/keystore/codigoch-release.jks`
- Alias: `codigoch`
- Contraseñas (uso académico): `codigoch2026`
- Configuración de firma: bloque `signingConfigs` en `app/build.gradle.kts`

(Agregar aquí la captura del APK firmado y del .jks)

## 7. Pruebas unitarias

JUnit 4 + kotlinx-coroutines-test, sin usar internet (se usan repositorios y API falsos):

- `ConsultasViewModelTest`: carga, validaciones, crear, editar, eliminar y manejo de errores.
- `ConsultaRepositoryImplTest`: respuestas correctas y fallas de red.
- `DerechoRepositoryImplTest`: búsqueda, filtros y favoritos.
- `DistanciaUtilsTest`: cálculo y formato de distancias.
