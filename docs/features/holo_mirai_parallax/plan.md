# [APROBADO] Plan técnico Holo Mirai

Fecha: 2026-10-05. Especificación: [spec.md](spec.md). Tareas: [tasks.md](tasks.md).

## Integración

- Rama `codex/holo-mirai-parallax`, desde master limpio. Mantener Koin, Coil 2 y un módulo :app.
- ML Kit segmentation-selfie 16.0.0-beta6, verificado en documentación y Maven de Google. Modelo incluido, API beta, unos 4,5 MB adicionales; latencia publicada no es garantía del dispositivo.
- Preferencia cifrada holoProfileEnabled=true compatible con JSON anterior, contrato Flow<Boolean> y MiraiLinkResult<Unit>.
- Controlador por pila; adaptadores de sensores y procesamiento separados de Compose; reglas puras comprobables.
- Un sensor activo: game rotation vector, rotation vector, gravedad o acelerómetro filtrado. Lecturas solicitadas a 50 Hz, postura neutral, suavizado y remapeo de ejes.
- Activación con pantalla reanudada y foco; pausa por interacción, modal, transición o salida. Respeto a sistema y ahorro de batería.
- Foto inmediata, preparación tras 150 ms estable. Bitmap software máximo 1024 píxeles por lado, SINGLE_IMAGE_MODE. Serializar trabajos reales aunque se cancele el consumidor.
- Máscara suavizada y misma geometria Crop en ambas capas. Validación conservadora de cobertura de silueta; alternativa simple si no apta. Fondo hasta 6 dp, sujeto hasta 3 dp opuesto (corrección de intensidad solicitada por el usuario tras probar la primera entrega).
- Caché solo en memoria, hasta 24 MB, clave por contenido/tamano/version; limpieza por memoria y sesión. Sin Room ni API nuevos.
- graphicsLayer con lecturas diferidas y Canvas para reflejo. Observación táctil sin consumo, sin cambios en umbrales ni acciones actuales.

## Ayuda y documentación

Cinco FAQ en CARDS_AND_MATCHING: descripción, desactivación, alternativas, gestos y offline/privacidad. Recursos en values, values-es y values-en, resueltos exclusivamente mediante stringResource en UI.

Actualizar guías de funcionamiento y maestra, README bilingue y documentación de privacidad. Distinguir procesamiento local de fotos de métricas propias del SDK.

## Verificación

Unitarias para reglas, preferencias, máscara, resultados obsoletos y recursos. Compose aislado con dobles locales para gestos y estados. Build, unitarias, lint y compilación instrumentada. Dispositivo para orientación, sensores, TalkBack, temas y calidad; dos móviles para comparación de tres recorridos de 30 swipes. No ejecutar E2E contra backend real de forma accidental.

## Fuentes

- https://developers.google.com/ml-kit/vision/selfie-segmentation/android
- https://developer.android.com/develop/sensors-and-location/sensors/sensors_position
- https://developer.android.com/develop/ui/compose/graphics/draw/modifiers
- https://developers.google.com/ml-kit/terms

El estado final de evidencia y sus límites se registra en [verification.md](verification.md).
