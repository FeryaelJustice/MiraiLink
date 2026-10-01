# [APROBADO] Plan Tecnico de Arquitectura: Mirai Studio y HUD Scanner Biometrico

- **Especificacion funcional asociada**: `docs/features/mirai_studio_hud_scanner/spec.md`
- **Estado**: [APROBADO]
- **Fecha**: 2026-10-01
- **Modulo**: `:app` (`com.feryaeljustice.mirailink`)

- - -

## 1. Hechos Verificados en el Proyecto (Sin Alucinaciones)

Informacion verificada rigurosamente en `gradle/libs.versions.toml` y `app/build.gradle.kts`:

- **Lenguaje & JVM**: Kotlin `2.4.10` / Java 17
- **Compilador & Build Tool**: AGP `9.4.1`, Gradle `9.6.1`, KSP `2.3.8`
- **SDK Targets**: Min SDK `30`, Compile SDK `37`, Target SDK `37`
- **Librerias Verificadas en el Classpath**:
  - UI: Compose BOM `2026.09.00` con Material 3 y Material Icons Core
  - Navegacion: Navigation 3 (`androidx.navigation3:navigation3-runtime:1.1.7`, `androidx.navigation3:navigation3-ui:1.1.7`)
  - Inyeccion de Dependencias: Koin BOM `4.2.2` (`koin-core`, `koin-android`, `koin-androidx-compose`)
  - Imagenes: Coil BOM `2.7.0` (`coil-compose`)
  - Persistencia Local: Room `2.8.5` y DataStore `1.2.1`
  - Serializacion: Kotlinx Serialization `1.11.0`
- **Nuevas Dependencias Requeridas (Verificadas en Maven Central / Google Maven)**:
  - Google ML Kit Face Detection (Bundled, 100% offline): `com.google.mlkit:face-detection:16.1.7`
  - AndroidX CameraX:
    - `androidx.camera:camera-core:1.4.1`
    - `androidx.camera:camera-camera2:1.4.1`
    - `androidx.camera:camera-lifecycle:1.4.1`
    - `androidx.camera:camera-view:1.4.1`

- - -

## 2. Impacto Arquitectonico y Contratos por Capas

### 2.1. Capa de Dominio (`domain/`)

- **Modelos de Negocio (`domain/model/studio/`)**:
  - `QualityBadge`: Enum o clase sellada que representa las insignias obtenidas:
    - `OPTIMAL_LIGHTING` ("Iluminacion Optima")
    - `DIRECT_GAZE` ("Mirada Directa")
    - `AUTHENTIC_SMILE` ("Sonrisa Autentica")
    - `CENTERED_FRAME` ("Encuadre Centrado")
    - `EYES_OPEN` ("Ojos Abiertos")
    - `HIGH_RESOLUTION` ("Alta Definicion")
    - `ANIME_OR_ART_APROVED` ("Arte / Ilustracion Aprobada")
  - `MetricStatus`: Estado de evaluacion (`EXCELLENT`, `ACCEPTABLE`, `WARNING`).
  - `FaceBiometrics`: Datos detectados del rostro (coordenadas relativas de bounding box, angulo yaw/pitch/roll, probabilidad de sonrisa, ojos abiertos, puntos clave).
  - `ImageQualityMetrics`: Metricas universales (luminancia promedio 0-100%, varianza de contraste, ancho/alto en px, flag `isLikelyScreenshot`, explicacion sugerida).
  - `MiraiScanResult`: Resultado consolidado del analisis (tipo: `FACIAL_PORTRAIT` o `VISUAL_ART_GENERAL`, lista de `QualityBadge`, metricas, `canProceedWithSoftWarning: Boolean`).

- **Casos de Uso (`domain/usecase/studio/`)**:
  - `AnalyzePhotoQualityUseCase`: Orquesta la evaluacion tanto de bitmaps estaticos como de datos de fotogramas, calculando metricas matematicas de calidad de imagen y combinandolas con el resultado de ML Kit Face Detection.

### 2.2. Capa de Datos (`data/`)

- **Procesamiento Local de Imagenes y Biometria (`data/studio/`)**:
  - `QualityMetricsCalculator`: Algoritmos locales puros para computar luminancia YUV/RGB, desviacion estandar de luminancia (contraste), deteccion heuristica de screenshot (proporciones de aspecto iguales al display o patron de nombre) y resolucion minima.
  - `FaceDetectorDataSource`: Encapsulacion de `com.google.mlkit.vision.face.FaceDetector` configurado con opciones de alta precision, clasificacion (sonrisa y ojos) y contornos para dibujo HUD.
  - `StudioPhotoAnalyzer`: Implementacion de `ImageAnalysis.Analyzer` para CameraX que procesa fotogramas a una cadencia controlada (descartando fotogramas redundantes).
- **FAQ Repository (`data/repository/FaqRepositoryImpl.kt`)**:
  - Incorporar la categoria `FaqCategory.PHOTOS_AND_STUDIO` con articulos didacticos:
    - `faq_studio_what_is`: ¿Que es Mirai Studio y como me ayuda a mejorar mis fotos?
    - `faq_studio_quality_standards`: ¿Cuales son los estandares de iluminacion, nitidez y encuadre?
    - `faq_studio_screenshots`: ¿Por que no se recomiendan capturas de pantalla con barras del sistema?
    - `faq_studio_anime_cosplay`: ¿Puedo subir fotos de anime, cosplay o ilustraciones?

### 2.3. Capa de Presentacion (`ui/`)

- **Navegacion (`ui/navigation/AppScreen.kt` y `NavWrapper.kt`)**:
  - `AppScreen.MiraiStudioScreen(val targetSlot: Int? = null)`
  - Permite navegar desde `SettingsScreen` (con `targetSlot = null` para laboratorio/calibracion) o desde `ProfileScreen` (con `targetSlot = 0..5` para sustitucion directa tras aprobacion).
- **Maquina de Estados (`ui/screens/studio/`)**:
  - `MiraiStudioViewModel`:
    - Gestiona el modo de operacion (`LIVE_CAMERA` vs `STATIC_IMAGE_REVIEW`).
    - Expone `StateFlow<MiraiStudioUiState>` (camara vinculada, permisos, resultado del analisis en vivo, foto capturada/seleccionada, veredicto final, guardado).
    - Permite al usuario capturar, alternar lente (frontal/trasera), importar foto de galeria, y confirmar ("Usar en Perfil" o "Guardar").
- **Componentes Graficos HUD Sci-Fi (`ui/screens/studio/components/`)**:
  - `CameraPreviewView`: Composable que envuelve `androidx.camera.view.PreviewView` con enlace al `LifecycleOwner`.
  - `LaserScanOverlay`: Barrido laser animado con gradiente neon (cyan/magenta) mediante `rememberInfiniteTransition`.
  - `RuleOfThirdsOverlay`: Reticula sutil de regla de los tercios con brackets sci-fi en las 4 esquinas.
  - `FaceBoxOverlay`: Caja delimitadora dinamica con puntos clave dibujados sobre el rostro detectado.
  - `HudMetricGauge`: Medidores graficos de simetria y nivel de luz con animacion de aguja/barra digital.
  - `QualityBadgesRow`: Insignias flotantes animadas con estilo holografico.
  - `HudQualityVerdictSheet`: Modal inferior / tarjeta flotante al congelar la foto con el desglose de calidad, advertencias suaves y botones ("Usar de todos modos", "Repetir", "Elegir otra").
- **Integracion en Pantallas Existentes**:
  - `SettingsScreen.kt`: Nueva tarjeta de accion "Mirai Studio (HUD Scanner)" en la seccion Multimedia / Cuenta.
  - `ProfileScreen.kt`:
    - En el dialogo de seleccion de foto: Boton/enlace "Ver estandares de calidad" (que navega o muestra el FAQ).
    - Al pulsar "Camara": Navega a `AppScreen.MiraiStudioScreen(targetSlot = slotIndex)` con camara activa.
    - Al pulsar "Galeria": Al elegir la foto, navega a `AppScreen.MiraiStudioScreen(targetSlot = slotIndex, initialUri = uri)` para mostrar el escaneo y veredicto HUD antes de asignarla.
  - `EditablePhotoGrid.kt`: Boton o badge discreto en cada ranura para re-escanear con Mirai Studio.

### 2.4. Inyeccion de Dependencias (`di/koin/`)

- En `appModule.kt` o `presentationModule.kt`:
  - `single { FaceDetectorDataSource() }`
  - `factory { AnalyzePhotoQualityUseCase(get(), get()) }`
  - `viewModel { MiraiStudioViewModel(get(), get(), get()) }`

- - -

## 3. Estrategia de Testing

- **Pruebas Unitarias (`app/src/test/`)**:
  - `QualityMetricsCalculatorTest`: Verifica la deteccion de baja luminancia, resolucion adecuada y deteccion de capturas de pantalla con bitmaps de prueba mockeados.
  - `AnalyzePhotoQualityUseCaseTest`: Verifica la asignacion correcta de insignias para fotos con rostro humano vs imagenes de anime/arte.
  - `MiraiStudioViewModelTest`: Pruebas de transiciones de estado con Turbine (`Init -> LiveAnalysis -> FrozenVerdict -> SelectedSlotConfirmed`).
- **Pruebas de Compilacion**:
  - `./gradlew assembleDebug` y `./gradlew testDebugUnitTest`.

- - -

## 4. Riesgos Tecnicos y Mitigaciones

- **Riesgo 1 (Rendimiento de procesamiento de fotogramas)**:
  - *Mitigacion*: En CameraX se usa `ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST` y se escala el analisis a resolucion media (480p/720p). Ademas se procesa en `Dispatchers.Default` sin saturar el hilo principal de Compose.
- **Riesgo 2 (Fuga de hardware de camara o ciclo de vida)**:
  - *Mitigacion*: El `ProcessCameraProvider` se desvincula en `DisposableEffect` y `onPause` de Android, liberando el sensor de inmediato.
- **Riesgo 3 (Rechazo involuntario de imagenes no humanas legitimas)**:
  - *Mitigacion*: El caso de uso evalua de forma inclusiva; si no hay rostro humano, evalua exclusivamente parametros visuales universales (luz, definicion, no captura de pantalla) y no bloquea al usuario ("Soft Warning").
