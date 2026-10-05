# [APROBADO] Plan tecnico Holo Mirai

Fecha: 2026-10-05. Especificacion: [spec.md](spec.md). Tareas: [tasks.md](tasks.md).

## Integracion

- Rama `codex/holo-mirai-parallax`, desde master limpio. Mantener Koin, Coil 2 y un modulo :app.
- ML Kit segmentation-selfie 16.0.0-beta6, verificado en documentacion y Maven de Google. Modelo incluido, API beta, unos 4,5 MB adicionales; latencia publicada no es garantia del dispositivo.
- Preferencia cifrada holoProfileEnabled=true compatible con JSON anterior, contrato Flow<Boolean> y MiraiLinkResult<Unit>.
- Controlador por pila; adaptadores de sensores y procesamiento separados de Compose; reglas puras comprobables.
- Un sensor activo: game rotation vector, rotation vector, gravedad o acelerometro filtrado. Lecturas solicitadas a 50 Hz, postura neutral, suavizado y remapeo de ejes.
- Activacion con pantalla reanudada y foco; pausa por interaccion, modal, transicion o salida. Respeto a sistema y ahorro de bateria.
- Foto inmediata, preparacion tras 150 ms estable. Bitmap software maximo 1024 pixeles por lado, SINGLE_IMAGE_MODE. Serializar trabajos reales aunque se cancele el consumidor.
- Mascara suavizada y misma geometria Crop en ambas capas. Validacion conservadora de cobertura de silueta; alternativa simple si no apta. Fondo hasta 4 dp, sujeto hasta 2 dp opuesto.
- Cache solo en memoria, hasta 24 MB, clave por contenido/tamano/version; limpieza por memoria y sesion. Sin Room ni API nuevos.
- graphicsLayer con lecturas diferidas y Canvas para reflejo. Observacion tactil sin consumo, sin cambios en umbrales ni acciones actuales.

## Ayuda y documentacion

Cinco FAQ en CARDS_AND_MATCHING: descripcion, desactivacion, alternativas, gestos y offline/privacidad. Recursos en values, values-es y values-en, resueltos exclusivamente mediante stringResource en UI.

Actualizar guias de funcionamiento y maestra, README bilingue y documentacion de privacidad. Distinguir procesamiento local de fotos de metricas propias del SDK.

## Verificacion

Unitarias para reglas, preferencias, mascara, resultados obsoletos y recursos. Compose aislado con dobles locales para gestos y estados. Build, unitarias, lint y compilacion instrumentada. Dispositivo para orientacion, sensores, TalkBack, temas y calidad; dos moviles para comparacion de tres recorridos de 30 swipes. No ejecutar E2E contra backend real de forma accidental.

## Fuentes

- https://developers.google.com/ml-kit/vision/selfie-segmentation/android
- https://developer.android.com/develop/sensors-and-location/sensors/sensors_position
- https://developer.android.com/develop/ui/compose/graphics/draw/modifiers
- https://developers.google.com/ml-kit/terms

El estado final de evidencia y sus limites se registra en [verification.md](verification.md).
