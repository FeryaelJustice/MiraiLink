# Mensajes, lectura y notificaciones

[Guía maestra](../../guia-maestra.md) | [Mapa de funcionalidades](../funcionalidades.md)

## Responsabilidad y recorrido

Messages lista conversaciones; ChatScreen usa userId para el historial privado. ChatViewModel obtiene sender/receiver, inicia consulta REST cada tres segundos y detiene su job al reset/cancelación. ChatApiService distingue chatId para read y userId para history: no intercambiarlos.

## Alternativas y efectos

Enviar llama REST y solo añade el mensaje local después de éxito. Si falla muestra error/reintento; no existe una cola persistida de mensajes remotos. La siguiente consulta reemplaza lista por datos servidor. Polling lento puede solaparse. startGroupMessagesPolling conserva TODO: rutas de grupos disponibles no prueban chat grupal completo en UI.

FCM avisa pero no sustituye lectura del historial. Autorizar membresía y almacenar mensajes es responsabilidad del [backend](https://github.com/FeryaelJustice/MiraiLink-Backend/blob/codex/documentacion-integral/docs/estudio/flujos/chat.md).

## Fuentes para estudiar

- [ChatViewModel.kt](../../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/chat/ChatViewModel.kt)
- [ChatApiService.kt](../../../app/src/main/java/com/feryaeljustice/mirailink/data/remote/ChatApiService.kt)
- [SocketService.kt](../../../app/src/main/java/com/feryaeljustice/mirailink/data/remote/socket/SocketService.kt)

Revisión: 2026-10-01, por inspección del código. Consultar [verificación](../desarrollo-y-verificacion.md) para resultados ejecutados.
