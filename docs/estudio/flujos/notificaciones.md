# FCM, registro de tokens y apertura desde notificación

[Guía maestra](../../guia-maestra.md) | [Mapa funcional](../funcionalidades.md)

Revisión: 2026-10-01, basada en código.

## Recorrido de registro

[FcmService.kt](../../../app/src/main/java/com/feryaeljustice/mirailink/service/FcmService.kt) recibe onNewToken, consulta currentAuth y espera hasta 1,5 segundos por autenticación. Si está autenticado llama SaveNotificationFCMUseCase; si no, el bloque solo contiene una propuesta comentada de persistencia. No hay cola durable en ese else. El servidor es responsable del almacenamiento del token y envío de push: ver [chat backend](https://github.com/FeryaelJustice/MiraiLink-Backend/blob/codex/documentacion-integral/docs/estudio/flujos/chat.md).

## Payload y presentación

El receptor interpreta data.type=new_message, conversationId, senderName y messagePreview; otros tipos no entran en showChatNotification. El PendingIntent abre MainActivity con NEW_TASK y CLEAR_TASK, sin URI ni conversationId. La notificación no abre por sí misma una conversación concreta ni carga historial.

NotificationCompat usa notification_fcm, mientras createNotificationChannel recibe el recurso default_notification_channel_id=fcm_default_channel. Esta divergencia debe resolverse y probarse en dispositivo antes de afirmar entrega visual. La creación del canal está en [MiraiLinkNotificationUtils.kt](../../../app/src/main/java/com/feryaeljustice/mirailink/notification/MiraiLinkNotificationUtils.kt). Si el ID no es numérico se genera un ID aleatorio entre 0 y 999; no garantiza agrupación estable por conversación.

## Errores, privacidad y permisos

El permiso del sistema, canales, estado foreground/background y clase del payload condicionan la recepción visible. FcmService registra token y RemoteMessage en logs; es una observación de privacidad, no un comportamiento corregido por esta documentación. Una respuesta HTTP de envío no acredita entrega FCM ni interacción del usuario.

Pruebas pendientes en dispositivo: permiso concedido/denegado, canales, rotación sin sesión, push foreground/background y apertura. No se han enviado notificaciones externas en esta entrega.
