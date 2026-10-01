# Integraciones del cliente y sus límites

[Guía maestra](../guia-maestra.md) | [Cobertura](cobertura.md)

Revisión de fuentes: 2026-10-01. Las observaciones estáticas no certifican el servidor desplegado ni el comportamiento en dispositivo.


## API y media

[NetworkModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/NetworkModule.kt) crea dos clientes: API con AuthInterceptor, idioma y timeouts de 10 segundos; imágenes con restricción de dominio y timeouts 15/20 segundos. Retrofit añade `/api/` al origen y usa Serialization. `Accept-Language` procede del Locale del dispositivo; la política de idioma/fallback del catálogo pertenece al backend.

SocketService está registrado, pero [ChatViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/chat/ChatViewModel.kt) usa REST para mensajes cada tres segundos. No hay evidencia de un servidor Socket.IO en el arranque backend revisado. FCM es notificación, no entrega de historial de chat ni persistencia de mensajes.

## Firebase e IA

`MainActivity` selecciona App Check por variante. `RemoteConfigManager` configura intervalo 3600 s y defaults XML; un fallo fetch se captura. `AiModule` construye Firebase AI y `GeminiDataSource` envía cada prompt aislado mediante generateContent; la lista visible vive en AiChatViewModel y no se adjunta como historial. No atribuir al backend estas llamadas del cliente ni afirmar que todo historial se guarda en PostgreSQL.

Analytics y Crashlytics se canalizan por adaptadores de telemetría. Revisar consentimientos y llamadas directas además de los adaptadores. `FcmService` recibe tokens/mensajes, y `SaveFcmUseCase` manda el registro al servidor cuando corresponde; el permiso denegado impide notificaciones visibles sin necesariamente invalidar la sesión.

## Ads, compras y cámara

AdMob y UMP coordinan anuncios y consentimiento en raíz UI/Activity. Los bucles de Ads dependen del lifecycle y configuración; no garantizar impresión de un anuncio por transcurrir un intervalo.

BillingClientManager consulta productos/ofertas y comunica eventos a SubscriptionRepositoryImpl; la app envía purchaseToken y producto al backend. El backend observado no verifica ese token con Google Play: ver [hallazgos](hallazgos.md). La intención de cancelar abre gestión de Google Play; cambiar una bandera backend no cancela facturación por sí solo.

Mirai Studio usa CameraX y ML Kit para detección facial, métricas de luminancia y decisiones de calidad. Las heurísticas de captura de pantalla no son prueba biométrica ni certificación antifraude. Ver [flujo de Studio](flujos/studio.md).

## Deep links

Hay esquemas HTTPS y `mirailink`. `MainActivity` recibe la URI y la sesión produce el destino consumido en navegación. Revisar path, parámetros y estado de sesión. Un enlace SMTP bien formado no garantiza dominio asociado en Android ni página web disponible.

Fuentes detalladas e importaciones: [referencia de código](referencia-codigo.md).
