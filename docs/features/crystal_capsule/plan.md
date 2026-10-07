# [BORRADOR] Plan Técnico de Arquitectura: Cápsula de Cristal v2 (Rediseño y Dinámica de Conexión)

- **Especificación funcional asociada**: `docs/features/crystal_capsule/spec.md`
- **Estado**: [BORRADOR]
- **Fecha**: 2026-10-07
- **Módulo**: `:app` (`com.feryaeljustice.mirailink`)

---

## 1. Hechos Verificados en el Proyecto (Sin Alucinaciones)

Información verificada rigurosamente en `gradle/libs.versions.toml` y `app/build.gradle.kts`:

- **Lenguaje & JVM**: Kotlin `2.4.20` / Java 17
- **Compilador & Build Tool**: AGP `9.4.1`, Gradle `9.6.1`, KSP `2.3.12`
- **SDK Targets**: Min SDK `26`, Compile SDK `37`, Target SDK `37`
- **Librerías Verificadas en el Classpath**:
  - UI: Jetpack Compose BOM `2026.09.00` con Material 3 y Compose Foundation `1.12.1`
  - Navegación: Navigation 3 (`androidx.navigation3:navigation3-core:1.2.0`)
  - Inyección de Dependencias: Koin BOM `4.2.2` con Koin Annotations
  - Persistencia Local: Room Database `2.8.5` (KSP) y Encrypted DataStore `1.2.1`
  - Red & Sockets: Retrofit `3.0.0`, OkHttp `5.5.0`, Socket.IO Client `2.1.2`
  - Serialización: Kotlinx Serialization `1.11.0`
  - Carga de Imágenes: Coil `2.7.0`

---

## 2. Impacto Arquitectónico y Contratos por Capas

### 2.1. Capa de Dominio (`domain/`)

#### Modelos (`domain/model/capsule/`)
- En `CrystalCapsule.kt`:
  - `CapsuleQuestion`: Soporte para respuestas bilaterales vinculadas (`ownAnswer: String?`, `peerAnswer: String?`, `isCustom: Boolean = false`).
  - `CrystalCapsule`:
    - Reducción del objetivo a 4 puntos (`progress: Int` en rango 0..4).
    - `level`: `progress.coerceIn(0, 4)`.
    - `activeQuestion`: Pregunta actualmente en curso (solo 1 activa permitida).
    - `completedQuestions: List<CompletedCapsuleQuestion>`: Historial de preguntas completadas con las dos respuestas.
  - `CapsuleAction`:
    - Soporte para acción de proponer pregunta: `category`, `questionText` (para personalizadas), `answerText` (respuesta obligatoria del proponente).
    - Soporte para acción de responder pregunta pendiente: `missionId`, `answerText`.

### 2.2. Capa de Datos (`data/`)

#### Repositorios (`data/repository/` y `data/repository/demo/`)
- `DemoCapsuleRepository.kt`:
  - Regla de puntuación: No se acreditan puntos por mensajes convencionales. Un punto se suma exclusivamente cuando ambos participantes responden a la misma pregunta.
  - Gestión de turno: Valida que solo haya 1 pregunta activa. Al responder el interlocutor (en modo Demo, respuesta simulada), la pregunta se marca como completada, se archiva en el histórico, suma 1 punto compartido y si `progress == 4` pasa a `revealed`.
  - Carga de preguntas desde `R.raw.crystal_capsule_catalog.json` filtrando 2-3 sugerencias por categoría.
- `CapsuleRepositoryImpl.kt`:
  - Consistencia de caché en DataStore para los nuevos campos de histórico y preguntas activas.

### 2.3. Capa de Presentación (`ui/`)

#### Componentes UI (`ui/components/`)
- **[MODIFY] `ChatTopBar.kt`**:
  - Incorporar el nuevo icono de cápsula a la izquierda del botón de reportar (`ic_report`).
  - Componente `CrystalCapsuleIcon`: Muestra un icono de cristal/cápsula con indicador visual de progreso circular o de llenado progresivo (0/4 vacío, 1/4, 2/4, 3/4 llenándose, 4/4 completamente lleno y resplandeciente).
  - Callback `onCapsuleClick: () -> Unit`.
- **[NEW] `CrystalCapsuleModal.kt`**:
  - Implementación con `ModalBottomSheet` de Material 3.
  - Máquina de estados interna (`enum class CapsuleModalStep { MAIN, NEW_QUESTION, HISTORY }`).
  - Cabecera: Título "Cápsula de Cristal", barra de progreso lineal temática y nivel actual.
  - Pantalla Principal:
    - Botón 1: "Preguntas e Historial" (con aviso de si hay pregunta pendiente de responder).
    - Botón 2: "Proponer Nueva Pregunta" (deshabilitado si ya hay una pregunta en curso con mensaje explicativo).
    - Footer con tarjetas descriptivas de opciones (pausar, abandonar, solicitar revelar).
    - Si la cápsula está completada (4/4): Botón de historial visible, botón de proponer oculto y banner de felicitación.
  - Pantalla Proponer Pregunta:
    - Selector de categorías.
    - Listado de 2-3 preguntas del catálogo + tarjeta "Crear mi propia pregunta" (máx. 120 caracteres).
    - Campo de respuesta obligatoria del usuario (máx. 300 caracteres).
    - Botón "Enviar" con validación de campos no vacíos.
  - Pantalla Historial:
    - Sección "Pendiente de responder": Pregunta recibida + campo de respuesta + botón enviar.
    - Sección "Esperando respuesta": Pregunta enviada por el usuario esperando al compañero.
    - Sección "Completadas": Tarjetas con la pregunta y las respuestas de ambos usuarios.
- **[MODIFY] `ChatScreen.kt`**:
  - Eliminar el componente fijo `CapsulePanel` del cuerpo del chat.
  - Integrar el control del BottomSheet modal activado desde `ChatTopBar`.
  - **Bloqueo de chat convencional**: Si la cápsula está activa (`status != "revealed"`), el campo de texto y la ruleta de gestos quedan bloqueados con aviso visual explicativo.
  - **Banner estático post-completado**: Al llegar a `revealed`, renderizar el banner estático de felicitación hasta que el primer mensaje real convencional sea enviado.
- **[MODIFY] `CrystalPhoto.kt`**:
  - Retirar las llamadas a `drawLine` en el `Canvas` de fracturas.
  - Conservar el desenfoque suave progresivo mediante `Modifier.blur`.

#### ViewModel (`ChatViewModel.kt`)
- Exponer el estado de la cápsula adaptado a 4 puntos y la pregunta activa.
- Flujo para proponer pregunta y responder pregunta.
- Persistencia del estado del banner de felicitación (`hasSentFirstChatMessage`).

---

## 3. Estrategia de Testing

- **Pruebas Unitarias (`app/src/test/`)**:
  - `DemoCapsuleRepositoryTest.kt`:
    - Validar que una pregunta respondida por 1 solo usuario no otorga puntos.
    - Validar que la respuesta bilateral a la misma pregunta otorga exactamente 1 punto.
    - Validar que no se puede proponer una segunda pregunta si ya hay una activa.
    - Validar que a los 4 puntos compartidos el estado cambia automáticamente a `revealed`.
  - `CrystalChatViewModelTest.kt`:
    - Validar las acciones de proponer y responder pregunta desde el ViewModel.
    - Validar la persistencia del banner post-desbloqueo.
- **Pruebas de Interfaz y Compilación**:
  - Compilación limpia con `./gradlew assembleDebug`.
  - Ejecución de la suite de pruebas: `./gradlew testDebugUnitTest`.

---

## 4. Riesgos Técnicos y Mitigaciones

- **Riesgo 1: Pérdida del texto en redacción si el usuario gira la pantalla mientras escribe su pregunta o respuesta.**
  - *Mitigación*: Emplear `rememberSaveable` para todos los estados de texto y navegación interna dentro del modal.
- **Riesgo 2: Inconsistencia si ambos usuarios envían una pregunta de forma casi concurrente.**
  - *Mitigación*: Validación de turnos e idempotencia en la capa de datos con verificación de `revision` de la cápsula.
