# Holo Mirai: funcionamiento, arquitectura y privacidad

[Guía maestra](../../guia-maestra.md) | [Spec aprobada](spec.md) | [Plan aprobado](plan.md) | [Tareas](tasks.md) | [Verificación](verification.md)

Revisión: 2026-10-05. El código y las pruebas acreditan únicamente lo descrito en el registro de verificación. Esta guía debe actualizarse con los cambios de contratos, configuración, persistencia y despliegue. La compilación y las pruebas simuladas no certifican resultados visuales, sensores físicos ni rendimiento real.

## Experiencia y alcance

Un pequeño movimiento reactivo y un reflejo de borde acompañan las tarjetas de Inicio y los feeds de Explorar, con estética coherente con la comunidad otaku y gamer. No es un modelo 3D ni reconstruye el fondo oculto. La foto original se muestra inmediatamente. `Static`, `SimpleParallax` y `SegmentedParallax` distinguen foto estática, movimiento de imagen completa y sujeto recortado sobre fondo natural. La segmentación solo se utiliza si una comprobación conservadora permite cubrir la silueta original durante el desplazamiento.

Solo la foto estable y visible de la tarjeta superior se prepara. Quedan excluidos placeholders, errores de carga, tarjetas inferiores, edición, previews y vista ampliada. Los componentes compartidos reciben un controlador opcional, desactivado por defecto. `UserSwipeCardStack` habilita el controlador en producción; los feeds comparten esa pila.

## Preferencias y localización

`AppPrefs.holoProfileEnabled` tiene valor inicial `true`, por lo que los datos cifrados antiguos siguen siendo compatibles. `MiraiLinkPrefs` actualiza el documento atómicamente sin reemplazar otras preferencias. `HoloPreferencesRepository` expone lectura reactiva y escritura con `MiraiLinkResult`; `SettingsViewModel` conserva el manejo de errores y reintento existente. La UI empieza desactivada hasta recibir preferencias y respeta el valor guardado. No requiere migración de Room ni sincronización con servidor.

En Ajustes > Apariencia aparece Perfil Holo-3D en demo y online. El interruptor tiene un único propietario de interacción, área mínima de 48 dp y estado accesible localizado. Cinco FAQ pertenecen a `CARDS_AND_MATCHING`: descripción, desactivación, alternativas, gestos y offline/privacidad. Modelos con referencias `@StringRes`, resolución con `stringResource` y recursos completos en `values`, `values-es` y `values-en`. Solo el original tiene descripción accesible; el recorte y Canvas son decorativos.

## Sensores, ciclo de vida e interacciones

`AndroidHoloMotionSource` prefiere game rotation vector, luego rotation vector, gravedad y acelerómetro filtrado. Solicita lecturas cada 20.000 microsegundos (50 Hz), calibra el primer valor, aplica filtro, zona muerta y límites normalizados. Para vectores de rotación calcula la matriz relativa a la postura neutral antes de extraer ángulos, evitando singularidades al sostener el teléfono vertical. Remapea ejes según rotación de pantalla y recalibra al cambiar orientación. Un único listener puede ser propietario en la app; `callbackFlow.awaitClose` lo libera.

`HoloRenderController` es un controlador por pila, creado por Koin. Combina preferencia, disponibilidad, política del dispositivo, ciclo de vida `RESUMED`, foco, foto lista y ausencia de cobertura modal. `AndroidHoloDevicePolicy` observa ahorro de batería y escala de animación; se consulta de nuevo al recuperar pantalla. Los observadores y receivers se liberan al terminar su suscripción.

`observeHoloTouch` observa el pase inicial sin consumir eventos. La inclinación se neutraliza durante pulsación, scroll, swipe, animación de salida y transición del carrusel. El encuadre ampliado se conserva durante el toque para evitar cambios de escala por cada pulsación. La preparación espera 150 ms de actividad estable. Los reconocedores, umbrales y callbacks de like/dislike/undo/carrusel no cambian. Holo no conoce acciones de voto ni calcula afinidad y no altera el controlador háptico.

## Imágenes, cancelación y memoria

El `ImageLoader` Coil 2 existente conserva clientes, validación de dominios y caché de originales. La solicitud Holo usa bitmap software, `Scale.FIT` y máximo 1.024 píxeles; el procesador limita de nuevo dimensiones y crea una copia ARGB propia. La copia se libera al terminar sin reciclar la imagen perteneciente a Coil.

`MlKitHoloImageProcessor` corre fuera del hilo principal, con un mutex. `SINGLE_IMAGE_MODE` y máscara cruda permiten interpolar confianza y crear alpha suavizado. El trabajo real del SDK termina dentro de `NonCancellable`, reteniendo el mutex: cancelar la UI no inicia otro análisis simultáneo. Los consumidores que esperan pueden cancelarse; el efecto de Compose conserva únicamente la solicitud actual. Al cambiar foto, los resultados antiguos no se aplican. El segmentador se cierra siempre al completar el Task.

La comprobación de cobertura rechaza confianza no válida, máscaras degeneradas y desplazamientos que descubran la silueta original. Acota conservadoramente la interpolación bilineal mediante el mínimo de los vecinos de todo el rectángulo continuo de movimiento, evitando miles de interpolaciones por píxel. La escala del recorte es 1,0525 y ambas capas comparten `ContentScale.Crop`. El fondo mueve como máximo 6 dp por eje; el sujeto, 3 dp en sentido opuesto. La alternativa simple mueve hasta 6 dp. El margen de escala depende del viewport para impedir bordes vacíos. No se rellena ni inventa fondo.

La caché LRU de derivados tiene presupuesto de 24 MB. Su clave SHA-256 incluye contenido de píxeles, dimensiones, viewport, densidad y versión `holo-v2`; también conserva alternativas simples. La generación cambia al limpiar: un trabajo previo no puede volver a poblarla. Presión de memoria, logout y cambios de usuario/demo la invalidan. No se reciclan bitmaps todavía referenciados por una composición. Tras limpiar, la foto actual suelta el recorte y no lo vuelve a preparar hasta cambiar de foto. No hay máscaras ni recortes persistentes.

Las transformaciones leen inclinación dentro de `graphicsLayer` y Canvas, evitando recomponer por cada lectura de sensor. El reflejo depende del movimiento y colores del tema; no hay partículas ni animación permanente de tiempo.

## Offline, privacidad y servidor

El modelo está incluido en la dependencia beta6 y no exige descargarlo para analizar. Offline requiere que el original esté disponible en la caché de imágenes existente; Holo no hace permanente una imagen remota ausente. Fotos y máscaras se procesan localmente. La feature no añade envíos de imágenes, métricas Holo, endpoints, DTO ni tablas. Por eso el backend no necesita modificaciones.

Google documenta métricas de utilización y rendimiento del SDK, que deben distinguirse del procesamiento de fotos en dispositivo. No afirmar que la app o ML Kit carecen de telemetría. Revisar la declaración de datos y consentimientos con las integraciones ya existentes antes de release. Fuentes oficiales consultadas el 2026-10-05: [segmentación Android](https://developers.google.com/ml-kit/vision/selfie-segmentation/android) y [términos y privacidad](https://developers.google.com/ml-kit/terms).

## Limitaciones y aceptación

La API sigue siendo beta. No se promete análisis en 50 ms ni segmentación correcta para todas las personas, avatares o fondos. La comprobación conservadora puede elegir movimiento simple incluso para un retrato. La apariencia de cabello, gafas, cosplay, grupos, paisajes y fotos pequeñas requiere revisión física en ambos temas. El objetivo de rendimiento es menos de 2 puntos adicionales de jank y menos del 20 % de degradación de p95 de frame, comparando tres recorridos de 30 swipes en dos móviles. Ese objetivo no se considera cumplido sin medición.
