# Checklist de Tareas: Mirai Studio y HUD Scanner Biométrico

- [x] **Fase 1: Configuración & Dependencias**
  - [x] Agregar versiones y librerías de CameraX (`1.4.1`) y Google ML Kit Face Detection (`16.1.7`) en `gradle/libs.versions.toml`
  - [x] Agregar dependencias correspondientes en `app/build.gradle.kts`
  - [x] Verificar compilación limpia preliminar con `./gradlew assembleDebug`

- [x] **Fase 2: Dominio (Modelos & Casos de Uso)**
  - [x] Crear modelos de dominio en `domain/model/studio/`: `QualityBadge`, `MetricStatus`, `FaceBiometrics`, `ImageQualityMetrics`, `MiraiScanResult`
  - [x] Crear caso de uso `AnalyzePhotoQualityUseCase` en `domain/usecase/studio/` con soporte para retratos faciales y arte/anime/cuerpo entero
  - [x] Crear pruebas unitarias para `AnalyzePhotoQualityUseCaseTest`

- [x] **Fase 3: Datos (Algoritmos Locales & Analizador)**
  - [x] Implementar `QualityMetricsCalculator` en `data/studio/` (luminancia YUV/RGB, desviación de contraste, resolución mínima, detección de capturas de pantalla)
  - [x] Implementar `FaceDetectorDataSource` en `data/studio/` con ML Kit Face Detection offline
  - [x] Implementar `StudioPhotoAnalyzer` en `data/studio/` para análisis en tiempo real con CameraX
  - [x] Actualizar `FaqCategory.kt` y `FaqRepositoryImpl.kt` con la categoría `PHOTOS_AND_STUDIO` y sus preguntas/respuestas

- [x] **Fase 4: Recursos Localizados (Strings & Estándares)**
  - [x] Añadir strings en `res/values/strings.xml` para insignias, métricas HUD, advertencias suaves, explicaciones de estándares y preguntas de FAQ
  - [x] Añadir strings equivalentes en `res/values-en/strings.xml`

- [x] **Fase 5: Presentación (Componentes HUD Sci-Fi & Pantalla)**
  - [x] Crear componentes visuales en `ui/screens/studio/components/`:
    - `LaserScanOverlay.kt` (laser animado con degradado neón)
    - `RuleOfThirdsOverlay.kt` (reticula de tercios y brackets sci-fi en esquinas)
    - `FaceBoxOverlay.kt` (caja delimitadora y puntos de anclaje)
    - `HudMetricGauge.kt` (medidores gráficos circulares y barras de luminancia/simetria)
    - `QualityBadgesRow.kt` (insignias holográficas animadas)
    - `HudQualityVerdictSheet.kt` (hoja de veredicto con botón de advertencia suave "Usar de todos modos")
    - `CameraPreviewView.kt` (visor CameraX con AndroidView)
  - [x] Implementar `MiraiStudioViewModel.kt` y su estado `MiraiStudioUiState.kt`
  - [x] Implementar la pantalla principal `MiraiStudioScreen.kt` con soporte para cámara frontal/trasera, galería y congelamiento de análisis

- [x] **Fase 6: Navegación e Integración con el Sistema**
  - [x] Declarar `AppScreen.MiraiStudioScreen` en `ui/navigation/AppScreen.kt` y registrar ruta en `NavWrapper.kt`
  - [x] Agregar acceso directo a Mirai Studio en `SettingsScreen.kt`
  - [x] Integrar Mirai Studio en `ProfileScreen.kt` al pulsar ranuras de fotos (Cámara y Galería)
  - [x] Agregar enlace/boton de estándares de calidad en el diálogo de origen de foto

- [x] **Fase 7: Inyección de Dependencias Koin & Tests Unitarios**
  - [x] Registrar componentes en los módulos de Koin (`dataModule.kt`, `useCaseModule.kt`, `viewModelModule.kt`)
  - [x] Implementar pruebas unitarias para `QualityMetricsCalculatorTest` y `AnalyzePhotoQualityUseCaseTest`

- [x] **Fase 8: Verificación Final y Calidad**
  - [x] Ejecutar `./gradlew testDebugUnitTest` y asegurar que todos los tests pasen
  - [x] Ejecutar `./gradlew assembleDebug` para garantizar compilación sin errores
  - [x] Verificar linter y ausencia de regresiones
