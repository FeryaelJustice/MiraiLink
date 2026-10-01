# Ciclo de vida, errores y recuperación Android

[Guía maestra](../guia-maestra.md) | [Cobertura](cobertura.md)

Revisión de fuentes: 2026-10-01. Las observaciones estáticas no certifican el servidor desplegado ni el comportamiento en dispositivo.


[SafeApiCall.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/util/SafeApiCall.kt) conserva CancellationException y clasifica otros fallos mediante NetworkErrorMapper. `safeApiUnitResponse` revisa el status HTTP, por lo que una Response Unit no equivale automáticamente a éxito. Las recuperaciones HTTP solo deben devolver un valor expresamente aprobado, no convertir cualquier error en datos vacíos.

[ChatViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/chat/ChatViewModel.kt) cancela polling en `stopMessagePolling`. El bucle llama a `getMessages`, que lanza otro job: con peticiones lentas pueden solaparse; el intervalo no es una garantía de exclusión mutua. El envío añade el mensaje a UI después de éxito REST, no antes. Un timeout después de escribir en servidor puede causar duplicado si se reintenta: la API no aporta un identificador idempotente de mensaje.

[GlobalMiraiLinkSession.kt](../../app/src/main/java/com/feryaeljustice/mirailink/state/GlobalMiraiLinkSession.kt) consulta presencia de foto con backoff de 10 a 120 segundos. Un error no demuestra ausencia de foto; distinguir dato antiguo de comprobación fallida. StateFlow y cachés viven en memoria; sesión cifrada sobrevive relanzamiento, pero borradores necesitan su mecanismo individual.

| Caso | Qué estudiar antes de diagnosticar |
| --- | --- |
| Sin conectividad o timeout | Taxonomía NetworkErrorMapper, acción de reintento y efectos ya confirmados |
| Token inválido/cuenta no verificada | Interceptor, estado pendiente, error Auth y autorización backend |
| Rotación/background | Scope, efecto Compose y pila de navegación; no basta con `remember` |
| Muerte de proceso | DataStore, Room y SavedStateHandle/rememberSaveable presentes en cada flujo |
| Datos vacíos | UI empty state; una lista vacía puede ser válida, no necesariamente error |
| Permiso rechazado | Ruta alternativa de cámara, ubicación y notificaciones |
| Payload corrupto | Serializer y consumidores; no hay recuperación general garantizada |
| Navegación desde URI | Parámetros, estado de sesión, destination y consumo del evento |

La especificación mobile contiene objetivos de accesibilidad y offline-first; documentarlos como objetivos cuando el código no los asegura. Para ampliar el sistema de errores: [contrato existente](../ai/error-handling.md).
