# [APROBADO] Plan Técnico de Arquitectura: Haptic Heartbeat (Radar de Sincronía Táctil y Sensorial)

- **Especificación funcional asociada**: `docs/features/haptic_heartbeat/spec.md`
- **Estado**: [APROBADO]
- **Fecha**: 2026-09-30
- **Módulo**: `:app` (`com.feryaeljustice.mirailink`)

- - -

## 1. Hechos Verificados en el Proyecto (Sin Alucinaciones)

Información verificada rigurosamente en `gradle/libs.versions.toml`, `app/build.gradle.kts` y manifiesto:

- **Lenguaje & JVM**: Kotlin `2.4.10` / Java 17 toolchain
- **Compilador & Build Tool**: AGP `9.4.1`, Gradle wrapper, KSP `2.3.8`
- **SDK Targets**: Min SDK `30`, Compile SDK `37`, Target SDK `37`
- **Librerías Verificadas en el Classpath**:
  - UI: Jetpack Compose BOM `2026.09.00` con Material 3 (`androidx.compose.material3`)
  - Animaciones y Gráficos: `androidx.compose.animation:animation`, Canvas API (`androidx.compose.ui.graphics.drawscope`)
  - Inyección de Dependencias: Koin BOM `4.2.2` con `koin-android` y `koin-compose-viewmodel`
  - Lifecycle: `androidx.lifecycle:lifecycle-runtime-compose:2.11.0`
- **Hardware y Permisos del Sistema**:
  - `VibratorManager` disponible nativamente en Android 12+ (API 31+).
  - `Vibrator` disponible nativamente en Android 11 (API 30, nuestro Min SDK).
  - `VibrationEffect.createWaveform(long[] timings, int[] amplitudes, int repeat)` y `VibrationEffect.createOneShot(long milliseconds, int amplitude)` son métodos estables de Android SDK desde API 26.
  - Permiso normal `<uses-permission android:name="android.permission.VIBRATE" />` debe ser incorporado en `AndroidManifest.xml`.

- - -

## 2. Impacto Arquitectónico y Contratos por Capas

### 2.1. Capa de Datos y Hardware (`data/` / `ui/haptics/`)
- **`AndroidManifest.xml`**:
  - Declaración de `<uses-permission android:name="android.permission.VIBRATE" />`.
- **Servicio de Feedback Háptico (`HapticHeartbeatController`)**:
  - Ubicación: `ui/haptics/HapticHeartbeatController.kt` (o `data/hardware/`).
  - Interfaz:
    ```kotlin
    interface HapticHeartbeatController {
        val isSupported: Boolean
        fun startHeartbeat(affinityRatio: Float)
        fun stopHeartbeat()
        fun triggerLikeConfirmation()
    }
    ```
  - Implementación `HapticHeartbeatControllerImpl(context: Context)`:
    - Resuelve `Vibrator` mediante `VibratorManager.defaultVibrator` en API >= 31 y `Context.VIBRATOR_SERVICE` en API 30.
    - Calcula el patrón cardíaco sístole/diástole (lub-dub):
      - Frecuencia modulada entre 60 BPM (baja afinidad) y 115 BPM (alta afinidad).
      - Modulación de amplitudes entre 80 y 255 si `hasAmplitudeControl() == true`.
      - Fallback por duración de pulsos si `hasAmplitudeControl() == false`.
    - `stopHeartbeat()`: invoca `vibrator.cancel()`.
    - `triggerLikeConfirmation()`: emite un pulso enérgico de confirmación al soltar tras el umbral.

### 2.2. Capa de Dominio (`domain/`)
- **Modelos de Negocio**:
  - `domain/model/haptics/HeartbeatAffinity.kt`:
    ```kotlin
    data class HeartbeatAffinity(
        val ratio: Float, // 0.0f a 1.0f
        val percentage: Int, // 0 a 100
        val bpm: Int, // e.g. 60 a 115
        val commonAnimesCount: Int,
        val commonGamesCount: Int,
        val commonGoalsCount: Int,
    )
    ```
- **Caso de Uso**:
  - `domain/usecase/haptics/CalculateHeartbeatAffinityUseCase.kt`:
    - Evalúa la intersección de IDs de `games`, `animes` y `relationshipGoals` entre el usuario logueado (`currentUser`) y el perfil evaluado (`candidate`).
    - Aplica un umbral base mínimo orgánico (0.20f) más el peso normalizado de coincidencias para garantizar que ningún perfil quede sin pulso perceptible.

### 2.3. Capa de Presentación (`ui/`)
- **Componente Canvas de Ondas Concéntricas (`HapticHeartbeatOverlay`)**:
  - Ubicación: `ui/components/haptics/HapticHeartbeatOverlay.kt`.
  - Animación sincronizada: calcula el progreso continuo del ciclo cardíaco en base al BPM de afinidad.
  - Proyección gráfica en Canvas:
    - Fondo oscurecido (`Color.Black.copy(alpha = 0.55f)`).
    - 3 a 4 anillos concéntricos con `Brush.radialGradient` o `Color` neón (rosa magenta `#FF2D55`, cian `#00F2FE`, violeta `#7928CA`).
    - Indicador central de sincronía ("XX% Sincronía") con circulo de progreso de carga hasta el umbral de 1.2 segundos.
- **Interacción Táctil en el Botón de Like (`HapticLikeButtonModifier` / `SwipeActionButton`)**:
  - En `UserSwipeCardStack.kt`: sustitución del botón de Like simple por un componente que soporte tap simple y pulsación mantenida con detección de tiempo (1.2s) mediante `pointerInput(Unit) { detectTapGestures(...) }` o `awaitEachGesture`.
  - Si se libera antes de 1.2s: se apaga el overlay y se cancela la vibración (modo radar previo).
  - Si se libera tras >= 1.2s: se dispara `triggerLikeConfirmation()` y se ejecuta `onLike()`.
  - Si se realiza un tap breve: se ejecuta `onLike()` de inmediato sin activar el overlay.
- **Integración en Pantallas**:
  - `HomeScreen.kt`: recibe `currentUser` de `HomeViewModel` y pasa la afinidad al overlay.
  - `CategoryFeedScreen.kt`: utiliza la misma botonera y contexto de `currentUser`.
  - `UserProfileDetailScreen.kt`: actualiza el botón inferior de Like con el mismo soporte sensorial.

### 2.4. Inyección de Dependencias (`di/koin/`)
- En `di/koin/AppModule.kt` o nuevo `di/koin/HapticsModule.kt`:
  - `single<HapticHeartbeatController> { HapticHeartbeatControllerImpl(androidContext()) }`
- En `di/koin/UseCaseModule.kt`:
  - `factory { CalculateHeartbeatAffinityUseCase() }`

- - -

## 3. Estrategia de Testing

- **Pruebas Unitarias (`app/src/test/`)**:
  - `CalculateHeartbeatAffinityUseCaseTest.kt`:
    - Validar calculo con perfiles sin coincidencias (debe arrojar ratio base ~0.20f y 60 BPM).
    - Validar calculo con perfiles con coincidencias totales (debe arrojar 1.0f y 115 BPM).
    - Validar calculo con usuario actual nulo (debe degradar a pulso neutro de forma segura sin null pointers).
  - `HapticHeartbeatControllerTest.kt`:
    - Simular llamadas con mocks de `Vibrator` y verificar invocaciones correctas a `vibrate()` y `cancel()`.
- **Compilación y Linter**:
  - `./gradlew assembleDebug`
  - `./gradlew testDebugUnitTest`
  - `./gradlew ktlintCheck`

- - -

## 4. Riesgos Técnicos y Mitigaciones

- **Riesgo 1: Fuga de vibración al abandonar la app durante la pulsación**:
  - *Mitigación*: El overlay y el controlador cancelan la vibración no solo en `onUp` sino también en `onCancel`, `onPause` y `DisposableEffect.onDispose`.
- **Riesgo 2: Retraso en UI o tirones de framerate en el Canvas**:
  - *Mitigación*: Las ondas se calculan como funciones matemáticas de radio y alfa en función del tiempo relativo en el bloque de dibujo de Canvas sin instanciaciones pesadas de objetos en cada frame.
- **Riesgo 3: Dispositivos sin hardware háptico**:
  - *Mitigación*: `isSupported` y comprobación de `hasVibrator()` envuelven las llamadas al sistema; el overlay visual de Canvas opera independientemente sin fallos.
