# Mirai Studio, cámara y calidad

[Guía maestra](../../guia-maestra.md) | [Mapa de funcionalidades](../funcionalidades.md)

## Responsabilidad y recorrido

CameraX entrega ImageProxy al StudioPhotoAnalyzer. Una guarda evita procesar varios frames a la vez; descarta/cierra frames ocupados. Calcula luminancia del plano Y, ejecuta detector ML Kit y cierra el proxy en finally. QualityMetricsCalculator muestrea bitmap, varianza, nombre y proporción para heurística de screenshot.

## Alternativas y efectos

Sin permiso o cámara no hay captura; imagen sin mediaImage se cierra. Si detección falla devuelve face null manteniendo luminancia. Las métricas de postura/calidad son ayudas, no verificación de identidad. El resultado pasa por modelos/usecase/ViewModel de Studio y puede entregar URI a perfil; la subida posterior sigue el contrato de fotos.

La decodificación/optimización debe cerrar recursos y limitar memoria; estudiar BitmapOptimizationUtils. No enviar datos al backend como si éste ejecutará ML Kit.

## Fuentes para estudiar

- [StudioPhotoAnalyzer.kt](../../../app/src/main/java/com/feryaeljustice/mirailink/data/studio/StudioPhotoAnalyzer.kt)
- [QualityMetricsCalculator.kt](../../../app/src/main/java/com/feryaeljustice/mirailink/data/studio/QualityMetricsCalculator.kt)
- [BitmapOptimizationUtils.kt](../../../app/src/main/java/com/feryaeljustice/mirailink/data/studio/BitmapOptimizationUtils.kt)

Revisión: 2026-10-01, por inspección del código. Consultar [verificación](../desarrollo-y-verificacion.md) para resultados ejecutados.
