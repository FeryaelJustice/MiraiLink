# [APROBADO] Plan Tecnico de Arquitectura: Ruleta de Gestos en Vivo (Desafio de Reaccion)

- **Especificacion funcional asociada**: `docs/features/gesture_roulette/spec.md`
- **Estado**: [APROBADO]
- **Fecha**: 2026-10-04
- **Modulo**: `:app` (`com.feryaeljustice.mirailink`)

- - -

## 1. Hechos Verificados en el Proyecto (Sin Alucinaciones)

Informacion verificada rigurosamente en `gradle/libs.versions.toml` y `app/build.gradle.kts`:

- **Lenguaje & JVM**: Kotlin `2.4.20` / Java 17
- **Compilador & Build Tool**: AGP `9.4.1`, Gradle wrapper, KSP `2.3.12`
- **SDK Targets**: Min SDK `30`, Compile SDK `37`, Target SDK `37`
- **Librerias de Vision & Camara en Classpath**:
  - `androidx.camera:camera-core:1.6.2`
  - `androidx.camera:camera-camera2:1.6.2`
  - `androidx.camera:camera-lifecycle:1.6.2`
  - `androidx.camera:camera-view:1.6.2`
  - `com.google.mlkit:face-detection:16.1.7`
  - `FaceDetectorDataSource.kt` ya operativo en `com.feryaeljustice.mirailink.data.studio`
  - `CameraPreviewView.kt` ya operativo en `com.feryaeljustice.mirailink.ui.screens.studio.components`
- **Librerias de UI y DI**:
  - Compose BOM `2026.09.00` con Material 3
  - Koin BOM `4.2.2`
  - Coroutines `1.10.2`
- **Backend**:
  - `MiraiLink-Backend` opera en Express REST sin servidor Socket.IO activo.
  - La mensajeria en `ChatViewModel` se realiza via llamadas REST (`sendMessageUseCase` y `getChatMessagesUseCase`).
  - No se modifica el backend; la feature es 100% cliente en Android.

- - -

## 2. Impacto Arquitectonico y Contratos por Capas

### 2.1. Capa de Dominio (`domain/`)
- **Modelos (`domain/model/chat/gesture/`)**:
  - `FaceGestureType`: Enum con los gestos soportados:
    - `WINK_LEFT`: Guiño de ojo izquierdo (`leftEyeOpenProbability <= 0.25f` && `rightEyeOpenProbability >= 0.70f`).
    - `WINK_RIGHT`: Guiño de ojo derecho (`rightEyeOpenProbability <= 0.25f` && `leftEyeOpenProbability >= 0.70f`).
    - `BIG_SMILE`: Sonrisa abierta (`smilingProbability >= 0.75f`).
    - `TILT_HEAD`: Ladeo de cabeza hacia cualquier lado (`abs(headEulerAngleZ) >= 18f`).
  - `GestureChallengeState`: Estado inmutable del reto:
    - Lista de 4 gestos fijados para la ronda actual.
    - Indice del gesto actual (0 a 3).
    - Tiempo restante (de 15.0f a 0.0f).
    - Estado de finalizacion (`CompletedSuccess`, `TimeExpired`, `Idle`, `Playing`).
    - Resultado final (`GestureChallengeSummary`: total gestos, tiempo total, porcentaje de compatibilidad 90-99%).
  - `GestureMessagePayload`: Parser y formateador seguro para identificar si un mensaje del chat es un reto o un resultado:
    - `formatInviteMessage()` -> Cadena estructurada amigable (ej. `[GESTURE_CHALLENGE_INVITE]`).
    - `formatResultMessage(summary)` -> Cadena estructurada con el resultado (ej. `[GESTURE_CHALLENGE_RESULT:score=4/4;time=8.2s;match=98%]`).
    - `parseMessage(content)` -> Retorna tipo de mensaje (`Regular`, `Invite`, `Result(summary)`).
- **Casos de Uso (`domain/usecase/chat/gesture/`)**:
  - `EvaluateFaceGestureUseCase`: Recibe `FaceBiometrics` y `FaceGestureType` y devuelve `Boolean` si se supera el umbral biometrico.
  - `CalculateGestureCompatibilityUseCase`: Calcula la afinidad y estadisticas de conexion segun los reflejos y el tiempo.

### 2.2. Capa de Datos (`data/`)
- **Analyzer de Camara (`data/studio/GesturePhotoAnalyzer.kt`)**:
  - Implementacion de `ImageAnalysis.Analyzer` que reutiliza `FaceDetectorDataSource`.
  - Procesa frames evitando sobrecarga mediante control de concurrencia no bloqueante (`@Volatile isProcessing`).
  - Notifica `FaceBiometrics?` a la capa de UI/ViewModel.

### 2.3. Capa de Presentacion (`ui/`)
- **Componentes (`ui/components/chat/gesture/`)**:
  - `GestureChallengeModal.kt`: Tarjeta modal interactiva centrada con:
    - Visor de camara frontal en vivo (`CameraPreviewView`).
    - Contador regresivo circular de 15 segundos.
    - Indicador animado del gesto a realizar con icono y texto grande.
    - Feedback visual instantaneo con check verde al superar cada uno de los 4 gestos.
    - Boton de cierre ("X") para salir en cualquier momento.
  - `ConfettiCelebration.kt`: Componente Canvas en Compose que despliega una lluvia de particulas multicolores con fisica balistica (gravedad, rotacion y dispersion) al ganar o finalizar el reto.
  - `GestureResultCard.kt`: Tarjeta de resultado con porcentaje de compatibilidad, badge de reflejos y boton "Compartir en chat".
  - `GestureChallengeMessageCard.kt`: Componente insertado en `ChatScreen` para renderizar burbujas interactivas:
    - Burbuja de invitacion con boton "¡Aceptar reto!".
    - Burbuja de tarjeta de compatibilidad con diseno de minijuego.
- **ViewModel (`ui/screens/chat/ChatViewModel.kt`)**:
  - `gestureGameUiState`: `StateFlow<GestureGameUiState>` con el progreso de los 4 gestos, temporizador y resultado.
  - Metodos: `startGestureGame()`, `onFaceBiometricsDetected(face)`, `sendGestureInvite()`, `shareGestureResult()`, `dismissGestureGame()`.
- **Integracion en `ChatScreen.kt`**:
  - Icono tematico en la barra de chat para lanzar el juego.
  - Solicitud fluida del permiso `CAMERA` antes de abrir el modal.
  - Renderizado condicional del modal `GestureChallengeModal` y del overlay de confeti.

- - -

## 3. Estrategia de Testing

- **Pruebas Unitarias (`app/src/test/`)**:
  - `EvaluateFaceGestureUseCaseTest`: Validar que cada gesto se detecte correctamente con diversos umbrales de `FaceBiometrics`.
  - `GestureMessagePayloadTest`: Validar parsing y formateo de mensajes sin romper compatibilidad con mensajes regulares de chat.
  - `CalculateGestureCompatibilityUseCaseTest`: Validar generacion de porcentajes y clasificaciones.
- **Compilacion**:
  - `./gradlew assembleDebug` y `./gradlew testDebugUnitTest`.

- - -

## 4. Riesgos Tecnicos y Mitigaciones

- **Riesgo 1: Fugas de memoria con CameraX**:
  - *Mitigacion*: El `ProcessCameraProvider` se desvincula en `onDispose` de `DisposableEffect` y cuando el modal se cierra.
- **Riesgo 2: Bloqueo de UI por analisis de frames**:
  - *Mitigacion*: El analisis de ML Kit corre en un executor de fondo (`Executors.newSingleThreadExecutor()`) y descarta frames si el anterior no ha terminado (`STRATEGY_KEEP_ONLY_LATEST`).
- **Riesgo 3: Permisos de camara no concedidos**:
  - *Mitigacion*: Verificacion previa con `rememberPermissionState` o `rememberLauncherForActivityResult`. Si no hay permiso, se solicita amigablemente antes de montar el componente de camara.
