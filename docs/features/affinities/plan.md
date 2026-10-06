# Plan aprobado de Afinidades

1. Validar compras con Google Play Developer API subscriptionsv2; registrar propiedad unica de tokens, estados y expiracion real. RTDN autenticadas y reconciliacion periodica. Reconocer despues de validacion.
2. Persistir actividad, resultados, preferencias, tandas, recomendaciones, likes con origen, solicitudes, bloqueos y outbox. Proteger rutas existentes de likes/chat y conservar origen historico.
3. Generador SQL con filtros mutuos y similitud Jaccard. Worker horario independiente e idempotente; outbox cada minuto. Revalidacion inmediata en lectura y acciones.
4. Integrar contratos Retrofit, repositorio, casos de uso, Koin y ViewModels. Una seccion Afinidades en Likes con Por descubrir e Interes recibido; solicitudes pendientes en Likes y conversaciones aceptadas en Mensajes; origen en Chat. Las tarjetas se retiran tras resolver, bloquear o denunciar. Demo aislada de compras y APIs reales.
5. Pruebas de permisos, pagos, concurrencia, seleccion, lifecycle y notificaciones. Documentar unidades systemd, configuracion, rollback y verificaciones pendientes.

Mantener Compose Material 3, Navigation 3, Koin y versiones instaladas. Estado del servidor autoritativo. No refactorizar chat a Socket.IO en este trabajo.
