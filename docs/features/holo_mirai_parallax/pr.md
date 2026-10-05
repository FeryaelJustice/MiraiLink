# Perfil Holo-3D local en tarjetas de descubrimiento

La foto visible de la tarjeta superior en Inicio y los feeds de Explorar reacciona a la inclinación con profundidad sutil y un reflejo de borde acorde al tema Mirai. Segmenta retratos localmente con ML Kit, valida la cobertura y recurre a movimiento de foto completa o foto estática cuando corresponde. Los textos y controles permanecen estables; conserva los reconocedores y callbacks de swipe, carrusel, vista ampliada, undo y latido.

Añade la preferencia cifrada Perfil Holo-3D, activa por defecto y compatible con datos anteriores; pausa por interacción, ciclo de vida, foco, ahorro de batería y animaciones deshabilitadas. La preparación espera 150 ms estables, serializa el trabajo real del SDK incluso bajo cancelación, limita bitmaps a 1.024 píxeles y conserva derivados solo en una caché de memoria de 24 MB, invalidada por memoria y sesión. Mantiene Koin, Coil 2 y el módulo actual, sin cambios de backend.

Incluye cinco FAQ en Búsqueda y tarjetas, 15 recursos completos en español e inglés, semántica única de foto/interruptor y documentación de arquitectura, privacidad y evidencia.

## Diseño aprobado y evidencia

- [Spec SDMD](https://github.com/FeryaelJustice/MiraiLink/blob/codex/holo-mirai-parallax/docs/features/holo_mirai_parallax/spec.md)
- [Plan](https://github.com/FeryaelJustice/MiraiLink/blob/codex/holo-mirai-parallax/docs/features/holo_mirai_parallax/plan.md)
- [Tareas](https://github.com/FeryaelJustice/MiraiLink/blob/codex/holo-mirai-parallax/docs/features/holo_mirai_parallax/tasks.md)
- [Verificación y límites](https://github.com/FeryaelJustice/MiraiLink/blob/codex/holo-mirai-parallax/docs/features/holo_mirai_parallax/verification.md)

## Validación

- Build debug y compilación de instrumentación correctos.
- 492 pruebas JVM y 19 pruebas instrumentadas Holo correctas en Android 15/API 35, con imágenes y servicios locales; no se ejecuta E2E contra usuarios reales.
- Gestos con efecto activo/apagado/pendiente, latido, pausa/resume, cache/cancelación, copia de bitmaps, políticas reales del emulador y FAQ/ajustes ES/EN.
- Capturas sintéticas claro/oscuro revisadas. No certifican calidad del recorte ni inclinación real.
- Lint ejecutado: 5 errores y 151 advertencias. Los errores ya existen en master: falta DAL de Credential Manager y porcentaje literal de la FAQ de facturación. Sin errores en Holo; no se añade baseline.

## Pendiente antes de integrar o publicar

Se entrega como borrador. Faltan revisión visual con retratos/cosplay/cabello/gafas/grupos/anime/paisajes, sensores y TalkBack físicos, recorrido offline de producción y comparación en dos móviles físicos con tres recorridos de 30 swipes por estado. No se acreditan todavía los objetivos de menos de 2 puntos adicionales de jank y menos del 20 % de degradación de frame p95. Resolver lint global y revisar declaración de datos/consentimientos antes de release.

El procesamiento local de fotos no implica ausencia de métricas de uso/diagnóstico de ML Kit u otros SDK. No se atribuyen garantías de producción o rendimiento a la compilación.
