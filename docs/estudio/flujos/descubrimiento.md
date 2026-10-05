# Feed, búsquedas, likes y matches

[Guía maestra](../../guia-maestra.md) | [Mapa de funcionalidades](../funcionalidades.md)

## Responsabilidad y recorrido

Home y preferencias de búsqueda separan borrador de filtro y feed. SwipeApiService usa limit/offset; like retorna información de match y dislike un resultado básico. MatchApiService diferencia matches y unseen, y mark-seen envía un ID. ReceivedLikes y detalle por username permiten otro recorrido hacia perfil.

El sistema de rebobinado (**Undo Swipe**) permite deshacer el último voto emitido (like o dislike) restaurando la tarjeta al tope de la pila en Home. La cuota diaria por nivel es estricta: 1 para Free, 3 para Plus y 6 para Premium, medida en una ventana deslizante de 24 horas. Si la cuota se agota (0 restantes), pulsar el botón de deshacer emite una navegación reactiva hacia el Paywall para ofrecer la mejora de suscripción.

## Integridad Temporal (Anti-Tampering)

Para evitar la manipulación de cuotas y fechas mediante la alteración manual del reloj, zona horaria o locale del dispositivo, la aplicación utiliza `TrustedTimeProvider`:
1. Prioridad: `TrustedTimeClient` de Google Play Services (`play-services-time:16.0.1`).
2. Fallback de red: Sincronización con la cabecera HTTP `Date` del backend interceptada por `ServerTimeInterceptor` en OkHttp.
3. Fallback de hardware: Anclaje al reloj monotónico (`SystemClock.elapsedRealtime()`) que avanza de manera continua independientemente de manipulaciones del reloj del sistema.

## Alternativas y efectos

Feed vacío puede ser válido por radio, exclusiones o falta de geografía; no eliminar filtros para ocultarlo. Permiso de ubicación rechazado afecta radius_active, no valida coordenadas ficticias. Preferencia specific_country conserva ID de país; cambiar scope limpia ese destino. El repositorio guarda servidor antes de DataStore y retorna error remoto.

El rebobinado soporta sesiones previas: si el historial inmediato en memoria está vacío, consulta al backend o a la base de datos local Room (`DemoSwipeHistoryDao`) para recuperar el último voto de días o sesiones anteriores. En modo Demo, la reversión y la auditoría de cuota se almacenan en `MiraiLinkDemoDatabase` (`DemoSwipeHistoryEntity` y `DemoSwipeUndoEntity`).

Demo usa sus repositorios locales; un match demo no existe en backend. Ranking, límites por suscripción, freshness y persistencia del match pertenecen al [servidor](https://github.com/FeryaelJustice/MiraiLink-Backend/blob/codex/documentacion-integral/docs/estudio/flujos/descubrimiento.md).

## Fuentes para estudiar

Holo Mirai se limita a la foto visible de la tarjeta superior en `UserSwipeCardStack`, compartida por Inicio y los feeds de Explorar. Los componentes compartidos tienen un controlador opcional, desactivado por defecto fuera de esas pilas. Los reconocedores y umbrales existentes conservan su responsabilidad; el observador Holo no consume eventos y nunca emite votos. La vista ampliada usa el original. Ver [arquitectura y alternativas](../../features/holo_mirai_parallax/implementation.md) y [evidencia](../../features/holo_mirai_parallax/verification.md).

- [SwipeApiService.kt](../../../app/src/main/java/com/feryaeljustice/mirailink/data/remote/SwipeApiService.kt)
- [TrustedTimeProvider.kt](../../../app/src/main/java/com/feryaeljustice/mirailink/data/time/TrustedTimeProvider.kt)
- [ServerTimeInterceptor.kt](../../../app/src/main/java/com/feryaeljustice/mirailink/data/remote/interceptor/ServerTimeInterceptor.kt)
- [HomeViewModel.kt](../../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/home/HomeViewModel.kt)
- [MatchApiService.kt](../../../app/src/main/java/com/feryaeljustice/mirailink/data/remote/MatchApiService.kt)
- [SearchPreferencesRepositoryImpl.kt](../../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/SearchPreferencesRepositoryImpl.kt)

Revisión: 2026-10-04, actualización del sistema de deshacer e integridad temporal. Consultar [verificación](../desarrollo-y-verificacion.md) para resultados ejecutados.
