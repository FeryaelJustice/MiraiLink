# [APROBADO] Plan Técnico de Arquitectura: Ruleta de Gestos en Vivo (Desafío de Reacción)

- **Especificación funcional asociada**: `docs/features/gesture_roulette/spec.md`
- **Estado**: [APROBADO]
- **Fecha**: 2026-10-04
- **Módulo**: `:app` (`com.feryaeljustice.mirailink`)

- - -

## 1. Hechos Verificados en el Proyecto (Sin Alucinaciones)

Información verificada rigurosamente en `gradle/libs.versions.toml` y `app/build.gradle.kts`:

- **Lenguaje & JVM**: Kotlin `2.4.20` / Java 17
- **Compilador & Build Tool**: AGP `9.4.1`, Gradle wrapper, KSP `2.3.12`
- **SDK Targets**: Min SDK `30`, Compile SDK `37`, Target SDK `37`
- **Librerías de Visión & Cámara en Classpath**:
  - `androidx.camera:camera-core:1.6.2`
  - `androidx.camera:camera-camera2:1.6.2`
  - `androidx.camera:camera-lifecycle:1.6.2`
  - `androidx.camera:camera-view:1.6.2`
  - `com.google.mlkit:face-detection:16.1.7`
  - `FaceDetectorDataSource.kt` ya operativo en `com.feryaeljustice.mirailink.data.studio`
  - `CameraPreviewView.kt` ya operativo en `com.feryaeljustice.mirailink.ui.screens.studio.components`
- **Librerías de UI y DI**:
  - Compose BOM `2026.09.00` con Material 3
  - Koin BOM `4.2.2`
  - Coroutines `1.10.2`
- **Backend**:
  - `MiraiLink-Backend` opera en Express REST sin servidor Socket.IO activo.
  - La mensajería en `ChatViewModel` se realiza vía llamadas REST (`sendMessageUseCase` y `getChatMessagesUseCase`).
  - No se modifica el backend; la feature es 100% cliente en Android.

- - -

## 2. Impacto Arquitectónico y Contratos por Capas

### 2.1. Capa de Dominio (`domain/`)
- **Modelos (`domain/model/chat/gesture/`)**:
  - `FaceGestureType`: Enum con los gestos soportados:
    - `WINK_LEFT`: Guiño de ojo izquierdo (`leftEyeOpenProbability <= 0.25f` && `rightEyeOpenProbability >= 0.70f`).
    - `WINK_RIGHT`: Guiño de ojo derecho (`rightEyeOpenProbability <= 0.25f` && `leftEyeOpenProbability >= 0.70f`).
    - `BIG_SMILE`: Sonrisa abierta (`smilingProbability >= 0.75f`).
    - `TILT_HEAD`: Ladeo de cabeza hacia cualquier lado (`abs(headEulerAngleZ) >= 18f`).
  - `GestureChallengeState`: Estado inmutable del reto:
    - Lista de 4 gestos fijados para la ronda actual.
    - Índice del gesto actual (0 a 3).
    - Tiempo restante (de 15.0f a 0.0f).
    - Estado de finalización (`CompletedSuccess`, `TimeExpired`, `Idle`, `Playing`).
    - Resultado final (`GestureChallengeSummary`: total gestos, tiempo total, porcentaje de compatibilidad 90-99%).
  - `GestureMessagePayload`: Parser y formateador seguro para identificar si un mensaje del chat es un reto o un resultado:
    - `formatInviteMessage()` -> Cadena estructurada amigable (ej. `[GESTURE_CHALLENGE_INVITE]`).
    - `formatResultMessage(summary)` -> Cadena estructurada con el resultado (ej. `[GESTURE_CHALLENGE_RESULT:score=4/4;time=8.2s;match=98%]`).
    - `parseMessage(content)` -> Retorna tipo de mensaje (`Regular`, `Invite`, `Result(summary)`).
- **Casos de Uso (`domain/usecase/chat/gesture/`)**:
  - `EvaluateFaceGestureUseCase`: Recibe `FaceBiometrics` y `FaceGestureType` y devuelve `Boolean` si se supera el umbral biométrico.
  - `CalculateGestureCompatibilityUseCase`: Calcula la afinidad y estadísticas de conexión según los reflejos y el tiempo.

### 2.2. Capa de Datos (`data/`)
- **Analyzer de Cámara (`data/studio/GesturePhotoAnalyzer.kt`)**:
  - Implementación de `ImageAnalysis.Analyzer` que reutiliza `FaceDetectorDataSource`.
  - Procesa frames evitando sobrecarga mediante control de concurrencia no bloqueante (`@Volatile isProcessing`).
  - Notifica `FaceBiometrics?` a la capa de UI/ViewModel.

### 2.3. Capa de Presentación (`ui/`)
- **Componentes (`ui/components/chat/gesture/`)**:
  - `GestureChallengeModal.kt`: Tarjeta modal interactiva centrada con:
    - Visor de cámara frontal en vivo (`CameraPreviewView`).
    - Contador regresivo circular de 15 segundos.
    - Indicador animado del gesto a realizar con icono y texto grande.
    - Feedback visual instantáneo con check verde al superar cada uno de los 4 gestos.
    - Botón de cierre ("X") para salir en cualquier momento.
  - `ConfettiCelebration.kt`: Componente Canvas en Compose que despliega una lluvia de partículas multicolores con física balística (gravedad, rotación y dispersión) al ganar o finalizar el reto.
  - `GestureResultCard.kt`: Tarjeta de resultado con porcentaje de compatibilidad, badge de reflejos y botón "Compartir en chat".
  - `GestureChallengeMessageCard.kt`: Componente insertado en `ChatScreen` para renderizar burbujas interactivas:
    - Burbuja de invitación con botón "¡Aceptar reto!".
    - Burbuja de tarjeta de compatibilidad con diseño de minijuego.
- **ViewModel (`ui/screens/chat/ChatViewModel.kt`)**:
  - `gestureGameUiState`: `StateFlow<GestureGameUiState>` con el progreso de los 4 gestos, temporizador y resultado.
  - Métodos: `startGestureGame()`, `onFaceBiometricsDetected(face)`, `sendGestureInvite()`, `shareGestureResult()`, `dismissGestureGame()`.
- **Integración en `ChatScreen.kt`**:
  - Icono temático en la barra de chat para lanzar el juego.
  - Solicitud fluida del permiso `CAMERA` antes de abrir el modal.
  - Renderizado condicional del modal `GestureChallengeModal` y del overlay de confeti.

- - -

## 3. Estrategia de Testing

- **Pruebas Unitarias (`app/src/test/`)**:
  - `EvaluateFaceGestureUseCaseTest`: Validar que cada gesto se detecte correctamente con diversos umbrales de `FaceBiometrics`.
  - `GestureMessagePayloadTest`: Validar parsing y formateo de mensajes sin romper compatibilidad con mensajes regulares de chat.
  - `CalculateGestureCompatibilityUseCaseTest`: Validar generación de porcentajes y clasificaciones.
- **Compilación**:
  - `./gradlew assembleDebug` y `./gradlew testDebugUnitTest`.

- - -

## 4. Riesgos Técnicos y Mitigaciones

- **Riesgo 1: Fugas de memoria con CameraX**:
  - *Mitigación*: El `ProcessCameraProvider` se desvincula en `onDispose` de `DisposableEffect` y cuando el modal se cierra.
- **Riesgo 2: Bloqueo de UI por análisis de frames**:
  - *Mitigación*: El análisis de ML Kit corre en un executor de fondo (`Executors.newSingleThreadExecutor()`) y descarta frames si el anterior no ha terminado (`STRATEGY_KEEP_ONLY_LATEST`).
- **Riesgo 3: Permisos de cámara no concedidos**:
  - *Mitigación*: Verificación previa con `rememberPermissionState` o `rememberLauncherForActivityResult`. Si no hay permiso, se solicita amigablemente antes de montar el componente de cámara.
