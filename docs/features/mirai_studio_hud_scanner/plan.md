# [APROBADO] Plan Técnico de Arquitectura: Mirai Studio y HUD Scanner Biométrico

- **Especificación funcional asociada**: `docs/features/mirai_studio_hud_scanner/spec.md`
- **Estado**: [APROBADO]
- **Fecha**: 2026-10-01
- **Módulo**: `:app` (`com.feryaeljustice.mirailink`)

- - -

## 1. Hechos Verificados en el Proyecto (Sin Alucinaciones)

Información verificada rigurosamente en `gradle/libs.versions.toml` y `app/build.gradle.kts`:

- **Lenguaje & JVM**: Kotlin `2.4.10` / Java 17
- **Compilador & Build Tool**: AGP `9.4.1`, Gradle `9.6.1`, KSP `2.3.8`
- **SDK Targets**: Min SDK `30`, Compile SDK `37`, Target SDK `37`
- **Librerías Verificadas en el Classpath**:
  - UI: Compose BOM `2026.09.00` con Material 3 y Material Icons Core
  - Navegación: Navigation 3 (`androidx.navigation3:navigation3-runtime:1.1.7`, `androidx.navigation3:navigation3-ui:1.1.7`)
  - Inyección de Dependencias: Koin BOM `4.2.2` (`koin-core`, `koin-android`, `koin-androidx-compose`)
  - Imágenes: Coil BOM `2.7.0` (`coil-compose`)
  - Persistencia Local: Room `2.8.5` y DataStore `1.2.1`
  - Serialización: Kotlinx Serialization `1.11.0`
- **Nuevas Dependencias Requeridas (Verificadas en Maven Central / Google Maven)**:
  - Google ML Kit Face Detection (Bundled, 100% offline): `com.google.mlkit:face-detection:16.1.7`
  - AndroidX CameraX:
    - `androidx.camera:camera-core:1.4.1`
    - `androidx.camera:camera-camera2:1.4.1`
    - `androidx.camera:camera-lifecycle:1.4.1`
    - `androidx.camera:camera-view:1.4.1`

- - -

## 2. Impacto Arquitectónico y Contratos por Capas

### 2.1. Capa de Dominio (`domain/`)

- **Modelos de Negocio (`domain/model/studio/`)**:
  - `QualityBadge`: Enum o clase sellada que representa las insignias obtenidas:
    - `OPTIMAL_LIGHTING` ("Iluminación Optima")
    - `DIRECT_GAZE` ("Mirada Directa")
    - `AUTHENTIC_SMILE` ("Sonrisa Auténtica")
    - `CENTERED_FRAME` ("Encuadre Centrado")
    - `EYES_OPEN` ("Ojos Abiertos")
    - `HIGH_RESOLUTION` ("Alta Definición")
    - `ANIME_OR_ART_APROVED` ("Arte / Ilustración Aprobada")
  - `MetricStatus`: Estado de evaluación (`EXCELLENT`, `ACCEPTABLE`, `WARNING`).
  - `FaceBiometrics`: Datos detectados del rostro (coordenadas relativas de bounding box, angulo yaw/pitch/roll, probabilidad de sonrisa, ojos abiertos, puntos clave).
  - `ImageQualityMetrics`: Métricas universales (luminancia promedio 0-100%, varianza de contraste, ancho/alto en px, flag `isLikelyScreenshot`, explicación sugerida).
  - `MiraiScanResult`: Resultado consolidado del análisis (tipo: `FACIAL_PORTRAIT` o `VISUAL_ART_GENERAL`, lista de `QualityBadge`, métricas, `canProceedWithSoftWarning: Boolean`).

- **Casos de Uso (`domain/usecase/studio/`)**:
  - `AnalyzePhotoQualityUseCase`: Orquesta la evaluación tanto de bitmaps estáticos como de datos de fotogramas, calculando métricas matemáticas de calidad de imagen y combinandolas con el resultado de ML Kit Face Detection.

### 2.2. Capa de Datos (`data/`)

- **Procesamiento Local de Imágenes y Biometria (`data/studio/`)**:
  - `QualityMetricsCalculator`: Algoritmos locales puros para computar luminancia YUV/RGB, desviación estándar de luminancia (contraste), detección heurística de screenshot (proporciones de aspecto iguales al display o patrón de nombre) y resolución mínima.
  - `FaceDetectorDataSource`: Encapsulación de `com.google.mlkit.vision.face.FaceDetector` configurado con opciones de alta precisión, clasificación (sonrisa y ojos) y contornos para dibujo HUD.
  - `StudioPhotoAnalyzer`: Implementación de `ImageAnalysis.Analyzer` para CameraX que procesa fotogramas a una cadencia controlada (descartando fotogramas redundantes).
- **FAQ Repository (`data/repository/FaqRepositoryImpl.kt`)**:
  - Incorporar la categoría `FaqCategory.PHOTOS_AND_STUDIO` con articulos didácticos:
    - `faq_studio_what_is`: ¿Qué es Mirai Studio y cómo me ayuda a mejorar mis fotos?
    - `faq_studio_quality_standards`: ¿Cuales son los estándares de iluminación, nitidez y encuadre?
    - `faq_studio_screenshots`: ¿Por que no se recomiendan capturas de pantalla con barras del sistema?
    - `faq_studio_anime_cosplay`: ¿Puedo subir fotos de anime, cosplay o ilustraciones?

### 2.3. Capa de Presentación (`ui/`)

- **Navegación (`ui/navigation/AppScreen.kt` y `NavWrapper.kt`)**:
  - `AppScreen.MiraiStudioScreen(val targetSlot: Int? = null)`
  - Permite navegar desde `SettingsScreen` (con `targetSlot = null` para laboratorio/calibracion) o desde `ProfileScreen` (con `targetSlot = 0..5` para sustitución directa tras aprobación).
- **Maquina de Estados (`ui/screens/studio/`)**:
  - `MiraiStudioViewModel`:
    - Gestiona el modo de operación (`LIVE_CAMERA` vs `STATIC_IMAGE_REVIEW`).
    - Expone `StateFlow<MiraiStudioUiState>` (cámara vinculada, permisos, resultado del análisis en vivo, foto capturada/seleccionada, veredicto final, guardado).
    - Permite al usuario capturar, alternar lente (frontal/trasera), importar foto de galería, y confirmar ("Usar en Perfil" o "Guardar").
- **Componentes Gráficos HUD Sci-Fi (`ui/screens/studio/components/`)**:
  - `CameraPreviewView`: Composable que envuelve `androidx.camera.view.PreviewView` con enlace al `LifecycleOwner`.
  - `LaserScanOverlay`: Barrido laser animado con gradiente neón (cyan/magenta) mediante `rememberInfiniteTransition`.
  - `RuleOfThirdsOverlay`: Reticula sutil de regla de los tercios con brackets sci-fi en las 4 esquinas.
  - `FaceBoxOverlay`: Caja delimitadora dinámica con puntos clave dibujados sobre el rostro detectado.
  - `HudMetricGauge`: Medidores gráficos de simetria y nivel de luz con animación de aguja/barra digital.
  - `QualityBadgesRow`: Insignias flotantes animadas con estilo holográfico.
  - `HudQualityVerdictSheet`: Modal inferior / tarjeta flotante al congelar la foto con el desglose de calidad, advertencias suaves y botones ("Usar de todos modos", "Repetir", "Elegir otra").
- **Integración en Pantallas Existentes**:
  - `SettingsScreen.kt`: Nueva tarjeta de acción "Mirai Studio (HUD Scanner)" en la sección Multimedia / Cuenta.
  - `ProfileScreen.kt`:
    - En el diálogo de selección de foto: Boton/enlace "Ver estándares de calidad" (que navega o muestra el FAQ).
    - Al pulsar "Cámara": Navega a `AppScreen.MiraiStudioScreen(targetSlot = slotIndex)` con cámara activa.
    - Al pulsar "Galería": Al elegir la foto, navega a `AppScreen.MiraiStudioScreen(targetSlot = slotIndex, initialUri = uri)` para mostrar el escaneo y veredicto HUD antes de asignarla.
  - `EditablePhotoGrid.kt`: Botón o badge discreto en cada ranura para re-escanear con Mirai Studio.

### 2.4. Inyección de Dependencias (`di/koin/`)

- En `appModule.kt` o `presentationModule.kt`:
  - `single { FaceDetectorDataSource() }`
  - `factory { AnalyzePhotoQualityUseCase(get(), get()) }`
  - `viewModel { MiraiStudioViewModel(get(), get(), get()) }`

- - -

## 3. Estrategia de Testing

- **Pruebas Unitarias (`app/src/test/`)**:
  - `QualityMetricsCalculatorTest`: Verifica la detección de baja luminancia, resolución adecuada y detección de capturas de pantalla con bitmaps de prueba mockeados.
  - `AnalyzePhotoQualityUseCaseTest`: Verifica la asignación correcta de insignias para fotos con rostro humano vs imágenes de anime/arte.
  - `MiraiStudioViewModelTest`: Pruebas de transiciones de estado con Turbine (`Init -> LiveAnalysis -> FrozenVerdict -> SelectedSlotConfirmed`).
- **Pruebas de Compilación**:
  - `./gradlew assembleDebug` y `./gradlew testDebugUnitTest`.

- - -

## 4. Riesgos Técnicos y Mitigaciones

- **Riesgo 1 (Rendimiento de procesamiento de fotogramas)**:
  - *Mitigación*: En CameraX se usa `ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST` y se escala el análisis a resolución media (480p/720p). Además se procesa en `Dispatchers.Default` sin saturar el hilo principal de Compose.
- **Riesgo 2 (Fuga de hardware de cámara o ciclo de vida)**:
  - *Mitigación*: El `ProcessCameraProvider` se desvincula en `DisposableEffect` y `onPause` de Android, liberando el sensor de inmediato.
- **Riesgo 3 (Rechazo involuntario de imágenes no humanas legitimas)**:
  - *Mitigación*: El caso de uso evalúa de forma inclusiva; si no hay rostro humano, evalúa exclusivamente parámetros visuales universales (luz, definición, no captura de pantalla) y no bloquea al usuario ("Soft Warning").
