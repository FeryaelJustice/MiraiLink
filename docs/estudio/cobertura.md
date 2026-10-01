# Matriz de cobertura y trazabilidad

[Guía maestra](../guia-maestra.md) | [Cobertura](cobertura.md)

Revisión de fuentes: 2026-10-01. Las observaciones estáticas no certifican el servidor desplegado ni el comportamiento en dispositivo.


| Área | Documento | Fuentes y límites |
| --- | --- | --- |
| Acceso, registro, verificación y recuperación | [Recorrido](flujos/acceso.md) | [AuthViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/AuthViewModel.kt), [UserApiService.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/remote/UserApiService.kt), [CredentialHelper.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/util/CredentialHelper.kt) |
| Perfil, residencia, fotos y atributos | [Recorrido](flujos/perfil.md) | [ProfileViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/ProfileViewModel.kt), [UserApiService.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/remote/UserApiService.kt), [UserRemoteDataSource.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/datasource/UserRemoteDataSource.kt) |
| Feed, búsquedas, likes y matches | [Recorrido](flujos/descubrimiento.md) | [SwipeApiService.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/remote/SwipeApiService.kt), [MatchApiService.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/remote/MatchApiService.kt), [SearchPreferencesRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/SearchPreferencesRepositoryImpl.kt) |
| Exploración por categorías | [Recorrido](flujos/exploracion.md) | [ExploreApiService.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/remote/ExploreApiService.kt), [DelegatingExploreRepository.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/delegating/DelegatingExploreRepository.kt) |
| Mensajes, lectura y notificaciones | [Recorrido](flujos/chat.md) | [ChatViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/chat/ChatViewModel.kt), [ChatApiService.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/remote/ChatApiService.kt), [SocketService.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/remote/socket/SocketService.kt) |
| Suscripciones, compras y gestión | [Recorrido](flujos/suscripciones.md) | [BillingClientManager.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/billing/BillingClientManager.kt), [SubscriptionRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/SubscriptionRepositoryImpl.kt), [SubscriptionApiService.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/remote/SubscriptionApiService.kt) |
| Mirai Studio, cámara y calidad | [Recorrido](flujos/studio.md) | [StudioPhotoAnalyzer.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/studio/StudioPhotoAnalyzer.kt), [QualityMetricsCalculator.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/studio/QualityMetricsCalculator.kt), [BitmapOptimizationUtils.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/studio/BitmapOptimizationUtils.kt) |
| Ajustes, FAQ, denuncias y feedback | [Recorrido](flujos/soporte.md) | [FeedbackApiService.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/remote/FeedbackApiService.kt), [ReportApiService.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/remote/ReportApiService.kt), [FaqRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/FaqRepositoryImpl.kt) |
| Tecnologías y versiones | [Tecnologías](tecnologias.md) | Catálogo/build o package.json; declaraciones, no despliegue |
| Arquitectura y consumidores | [Arquitectura](arquitectura.md), [referencia](referencia-codigo.md) | Fuentes propias versionadas; extractor estático con límites |
| Configuración | [Semántica](configuracion.md), [lectores](lectores-configuracion.md) | Esquemas y consumidores sin secretos |
| Seguridad | [Seguridad](autenticacion-y-seguridad.md) | Contratos, hashes/cifrado y autorización por propietario |
| Errores y casos alternativos | [Errores](ciclo-de-vida-y-errores.md) | Guards, callbacks, cancelación y resultados reales |
| Servicios externos | [Integraciones](integraciones.md) | Sin certificar proveedores ni entrega |
| Persistencia | [Demo/DataStore](persistencia-y-demo.md) | Room v3 y preferencias; no sync remoto implícito |
| Diagramas | [Índice](diagramas/indice.md) | Tipo, ubicación y fuente por vista |
| Estado de validación | [Verificación](desarrollo-y-verificacion.md) | Resultados con límites y fecha |

## Cobertura de archivos

La [referencia navegable](referencia-codigo.md) enumera todas las fuentes propias Kotlin o JavaScript del inventario versionado y muestra declaraciones, dependencias importadas y consumidores detectados. El extractor no resuelve reflexión, DI, lambdas ni dispatch dinámico; los documentos temáticos explican el recorrido real de los dominios. Esta referencia complementa comentarios explicativos, no los sustituye.

Quedan fuera dependencias, binarios, código generado y archivos privados. No se leen local.properties, material de firma ni configuración privada Firebase. No afirmar cobertura de pruebas del 100% por enumerar todos los archivos.

- [Arranque, onboarding, versión y banderas](flujos/arranque.md): recorrido propio y límites de integración revisados.

- [Chat IA](flujos/ia.md): recorrido propio y límites de integración revisados.

- [Registro FCM y notificaciones](flujos/notificaciones.md): recorrido propio y límites de integración revisados.


## Cierre de verificación

[Comentarios comparados](comentarios-verificados.md) y [resultados ejecutados](desarrollo-y-verificacion.md). La cobertura documental no implica cobertura completa de pruebas ni verificación de producción.
